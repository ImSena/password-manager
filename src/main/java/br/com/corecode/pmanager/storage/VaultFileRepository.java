package br.com.corecode.pmanager.storage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;

import javax.crypto.SecretKey;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.corecode.pmanager.crypto.CryptoConfig;
import br.com.corecode.pmanager.crypto.CryptoService;
import br.com.corecode.pmanager.crypto.KeyDerivationService;
import br.com.corecode.pmanager.crypto.SecureRandomService;
import br.com.corecode.pmanager.domain.Vault;

public class VaultFileRepository {
    private static final byte[] MAGIC = new byte[] { 0x50, 0x4D, 0x56, 0x31 };

    private final ObjectMapper mapper = new ObjectMapper();

    public void create(Path path, char[] masterPassword) {
        save(path, new Vault(), masterPassword);
    }

    public Vault open(Path path, char[] masterPassword) {
        try (DataInputStream in = new DataInputStream(Files.newInputStream(path))) {

            validateMagic(in);

            byte version = in.readByte();
            if (version != CryptoConfig.FILE_VERSION) {
                throw new RuntimeException("Versão de arquivo incompátivel. Esperado: "
                        + CryptoConfig.FILE_VERSION + ", Recebido: " + version);

            }

            byte[] salt = readBytes(in);
            byte[] iv = readBytes(in);

            int cipherLen = in.readInt();
            byte[] cipher = in.readNBytes(cipherLen);

            SecretKey key = KeyDerivationService.deriveKey(masterPassword, salt);
            byte[] plain = CryptoService.decrypt(cipher, key, iv);

            return mapper.readValue(plain, Vault.class);

        } catch (Exception e) {
            throw new RuntimeException("Erro ao abrir cofre", e);
        }
    }

    public void save(Path path, Vault vault, char[] masterPassword) {
        try {

            byte[] salt = SecureRandomService.generateBytes(CryptoConfig.SALT_LENGTH);
            byte[] iv = SecureRandomService.generateBytes(CryptoConfig.IV_LENGTH);

            SecretKey key = KeyDerivationService.deriveKey(masterPassword, salt);
            byte[] plain = mapper.writeValueAsBytes(vault);

            byte[] cipher = CryptoService.encrypt(plain, key, iv);

            writeFile(path, salt, iv, cipher);

        } catch (Exception e) {
            throw new RuntimeException("Erro ao salvar cofre", e);
        }
    }

    private void writeFile(Path path, byte[] salt, byte[] iv, byte[] cipher) throws IOException {
        Path tempPath = path.resolveSibling(path.getFileName() + ".tmp");
        try (DataOutputStream out = new DataOutputStream(Files.newOutputStream(tempPath))) {
            out.write(MAGIC);
            out.writeByte(CryptoConfig.FILE_VERSION);

            writeBytes(out, salt);
            writeBytes(out, iv);

            out.writeInt(cipher.length);
            out.write(cipher);
        }

        Files.move(tempPath, path, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
    }

    private void validateMagic(DataInputStream in) throws IOException {
        byte[] magic = in.readNBytes(4);
        if (!Arrays.equals(magic, MAGIC)) {
            throw new RuntimeException("Arquivo inválido");
        }
    }

    private byte[] readBytes(DataInputStream in) throws IOException {
        int len = in.readUnsignedShort();
        return in.readNBytes(len);
    }

    private void writeBytes(DataOutputStream out, byte[] bytes) throws IOException {
        out.writeShort(bytes.length);
        out.write(bytes);
    }
}
