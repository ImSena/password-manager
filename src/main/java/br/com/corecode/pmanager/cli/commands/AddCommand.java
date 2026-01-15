package br.com.corecode.pmanager.cli.commands;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.nio.file.Files;
import java.util.Arrays;

import javax.crypto.SecretKey;

import br.com.corecode.pmanager.cli.Command;
import br.com.corecode.pmanager.cli.CommandContext;
import br.com.corecode.pmanager.cli.SecureInput;
import br.com.corecode.pmanager.crypto.CryptoConfig;
import br.com.corecode.pmanager.crypto.CryptoService;
import br.com.corecode.pmanager.crypto.MemorySafeUtils;
import br.com.corecode.pmanager.crypto.SecureRandomService;
import br.com.corecode.pmanager.domain.PasswordEntry;
import br.com.corecode.pmanager.session.VaultSession;

public class AddCommand implements Command {

    @Override
    public String name() {
        return "add";
    }

    @Override
    public void execute(CommandContext context) {
        char[] idChar = null;
        char[] user = null;
        char[] password = null;
        char[] notes = null;

        byte[] userBytes = null;
        byte[] passBytes = null;
        byte[] notesBytes = null;
        byte[] plainPayload = null;
        byte[] encryptedPayload = null;
        try {
            if (!Files.exists(context.vaultPath())) {
                System.out.println("Cofre não existe. Execute 'init' primeiro");
                return;
            }

            VaultSession session = context.session();

            String id;

            while (true) {
                System.out.println("\n Digite as credenciais \n");
                idChar = SecureInput.readChars("ID: ");
                id = new String(idChar);

                user = SecureInput.readChars("Usuário: ");
                password = SecureInput.readChars("Senha: ");
                notes = SecureInput.readChars("Descrição (opcional): ");

                System.out.print("As credenciais estão corretas? (s/n)");
                String response;

                try {
                    response = context.scanner().nextLine();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

                while (response.isEmpty()) {
                    System.out.println("Entrada inválida");
                    System.out.print("As credenciais estão corretas? (s/n)");
                    try {
                        response = context.scanner().nextLine();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }

                if(response.equalsIgnoreCase("s")){
                    break;
                }
            }

            userBytes = MemorySafeUtils.toBytes(user);
            passBytes = MemorySafeUtils.toBytes(password);
            notesBytes = MemorySafeUtils.toBytes(notes);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(baos);

            dos.writeInt(userBytes.length);
            dos.write(userBytes);
            dos.writeInt(passBytes.length);
            dos.write(passBytes);
            dos.writeInt(notesBytes.length);
            dos.write(notesBytes);

            plainPayload = baos.toByteArray();

            byte[] entryIv = SecureRandomService.generateBytes(CryptoConfig.IV_LENGTH);
            SecretKey sessionKey = context.session().getKey();

            encryptedPayload = CryptoService.encrypt(plainPayload, sessionKey, entryIv);

            PasswordEntry entry = new PasswordEntry(id, encryptedPayload, entryIv);
            context.session().getVault().add(entry);

            context.repository().save(context.vaultPath(), session.getVault(), session.getKey(), session.getSalt());

            System.out.println("Credencial adicionada com sucesso. ");

        } catch (Exception e) {
            System.out.println("Erro ao adicionar credencial: " + e.getMessage());
        } finally {
            if (idChar != null)
                Arrays.fill(idChar, '\0');
            if (user != null)
                Arrays.fill(user, '\0');
            if (password != null)
                Arrays.fill(password, '\0');
            if (notes != null)
                Arrays.fill(notes, '\0');

            if (userBytes != null)
                Arrays.fill(userBytes, (byte) 0);
            if (passBytes != null)
                Arrays.fill(passBytes, (byte) 0);
            if (notesBytes != null)
                Arrays.fill(notesBytes, (byte) 0);
            if (plainPayload != null)
                Arrays.fill(plainPayload, (byte) 0);
        }
    }

}
