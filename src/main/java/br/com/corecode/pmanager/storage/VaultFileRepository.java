package br.com.corecode.pmanager.storage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;

import javax.crypto.AEADBadTagException;
import javax.crypto.SecretKey;
import com.fasterxml.jackson.databind.ObjectMapper;
import br.com.corecode.pmanager.crypto.CryptoConfig;
import br.com.corecode.pmanager.crypto.CryptoService;
import br.com.corecode.pmanager.crypto.DerivedKey;
import br.com.corecode.pmanager.crypto.KeyDerivationService;
import br.com.corecode.pmanager.crypto.SecureRandomService;
import br.com.corecode.pmanager.domain.Vault;

public class VaultFileRepository {
    private static final byte[] MAGIC = new byte[] { 0x50, 0x4D, 0x56, 0x31 };

    private final ObjectMapper mapper = new ObjectMapper();

    public void create(Path path, char[] masterPassword) {
        DerivedKey derived = KeyDerivationService.derivedKeyForNewVault(masterPassword);
        save(path, new Vault(), derived.key(), derived.salt());
    }

    public VaultHandle open(Path path, char[] masterPassword) {
        try {
            return loadFromFile(path, masterPassword);
        } catch (Exception e) {
            Path backupPath = path.resolveSibling(path.getFileName() + ".bak");

            if (Files.exists(backupPath)) {
                System.out.println("\n ALERTA: Arquivo principal corrompido ou senha incorreta.");
                System.out.println("🔄 Tentando recuperar do backup automático...");

                try {
                    VaultHandle handle = loadFromFile(backupPath, masterPassword);

                    System.out.println("✅ Backup íntegro encontrado! Restaurando...");
                    Files.copy(backupPath, path, StandardCopyOption.REPLACE_EXISTING);
                    System.out.println("✅ Cofre recuperado com sucesso.");

                    return handle;

                } catch (Exception exBackup) {
                    throw new RuntimeException("Falha na autenticação (Senha incorreta ou arquivos irrecuperáveis).");
                }
            }

            throw e;
        }
    }

    private VaultHandle loadFromFile(Path path, char[] masterPassword) {
        try (DataInputStream in = new DataInputStream(Files.newInputStream(path))) {
            validateMagic(in);

            byte version = in.readByte();
            if (version != CryptoConfig.FILE_VERSION) {
                throw new RuntimeException("Versão incompatível.");
            }

            byte[] salt = readBytes(in);
            byte[] iv = readBytes(in);
            byte[] cipher;
            try {
                int cipherLen = in.readInt();
                cipher = new byte[cipherLen];
                in.readFully(cipher);
            } catch (Exception e) {
                throw new AEADBadTagException(e.getMessage());
            }

            SecretKey key = KeyDerivationService.deriveKey(masterPassword, salt);
            byte[] plain = CryptoService.decrypt(cipher, key, iv);

            Vault vault = mapper.readValue(plain, Vault.class);
            return new VaultHandle(vault, key, salt);

        } catch (AEADBadTagException e) {
            throw new RuntimeException("Erro de Autenticação/Integridade", e);
        } catch (Exception e) {
            throw new RuntimeException("Erro de Leitura: " + e.getMessage(), e);
        }
    }

    public void save(Path path, Vault vault, SecretKey key, byte[] salt) {
        try {
            byte[] iv = SecureRandomService.generateBytes(CryptoConfig.IV_LENGTH);
            byte[] plain = mapper.writeValueAsBytes(vault);
            byte[] cipher = CryptoService.encrypt(plain, key, iv);

            writeFile(path, salt, iv, cipher);

        } catch (Exception e) {
            throw new RuntimeException("Erro ao salvar cofre", e);
        }
    }

    private void writeFile(Path targetPath, byte[] salt, byte[] iv, byte[] cipher) throws IOException {
        Path tempPath = targetPath.resolveSibling(targetPath.getFileName() + ".tmp");
        Path backupPath = targetPath.resolveSibling(targetPath.getFileName() + ".bak");

        try (
                FileOutputStream fos = new FileOutputStream(tempPath.toFile());
                DataOutputStream out = new DataOutputStream(fos)) {
            out.write(MAGIC);
            out.writeByte(CryptoConfig.FILE_VERSION);
            writeBytes(out, salt);
            writeBytes(out, iv);
            out.writeInt(cipher.length);
            out.write(cipher);
            out.flush();
            fos.getFD().sync();
        }

        if (Files.exists(targetPath)) {
            Files.move(targetPath, backupPath, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
        }

        Files.move(tempPath, targetPath, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
    }

    private void validateMagic(DataInputStream in) throws IOException {
        byte[] magic = new byte[MAGIC.length];
        in.readFully(magic);
        if (!Arrays.equals(magic, MAGIC)) {
            throw new RuntimeException("Arquivo inválido");
        }
    }

    private byte[] readBytes(DataInputStream in) throws IOException {
        int len = in.readUnsignedShort();
        byte[] b = new byte[len];
        in.readFully(b);
        return b;
    }

    private void writeBytes(DataOutputStream out, byte[] bytes) throws IOException {
        if (bytes.length > 65535)
            throw new IOException("Bloco de dados muito grande");
        out.writeShort(bytes.length);
        out.write(bytes);
    }
}
