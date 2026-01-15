package br.com.corecode.pmanager.cli.commands;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.nio.file.Files;
import java.util.Arrays;

import br.com.corecode.pmanager.cli.Command;
import br.com.corecode.pmanager.cli.CommandContext;
import br.com.corecode.pmanager.crypto.CryptoService;
import br.com.corecode.pmanager.domain.PasswordEntry;
import br.com.corecode.pmanager.session.VaultSession;

public class GetCommand implements Command {

    @Override
    public String name() {
        return "get";
    }

    @Override
    public void execute(CommandContext context) {
        byte[] decryptedPayload = null;
        byte[] userBytes = null;
        byte[] passBytes = null;
        byte[] notesBytes = null;
        try {
            if (context.args().length < 2) {
                System.out.println("Uso: pmanager get <id>");
                return;
            }

            if (!Files.exists(context.vaultPath())) {
                System.out.println("Cofre não existe.");
                return;
            }

            VaultSession session = context.session();

            String id = context.args()[1];

            PasswordEntry entry = session.getVault().get(id);

            if (entry == null) {
                System.out.println("Credencial não encontrada: " + id);
                return;
            }

            decryptedPayload = CryptoService.decrypt(
                    entry.getEncryptedData(),
                    context.session().getKey(),
                    entry.getIv());

            DataInputStream dis = new DataInputStream(new ByteArrayInputStream(decryptedPayload));

            int userLen = dis.readInt();
            userBytes = dis.readNBytes(userLen);

            int passLen = dis.readInt();
            passBytes = dis.readNBytes(passLen);

            int notesLen = dis.readInt();
            notesBytes = dis.readNBytes(notesLen);

            System.out.println("ID: " + entry.getId());

            printSafe("Username: ", userBytes);
            printSafe("Password: ", passBytes);
            printSafe("Notes: ", notesBytes);

        } catch (Exception e) {
            System.out.println("Erro ao obter credencial: " + e.getMessage());
        } finally {
            if (decryptedPayload != null) {
                Arrays.fill(decryptedPayload, (byte) 0);
            }
            if (userBytes != null) {
                Arrays.fill(userBytes, (byte) 0);
            }
            if (passBytes != null) {
                Arrays.fill(passBytes, (byte) 0);
            }
            ;
            if (notesBytes != null) {
                Arrays.fill(notesBytes, (byte) 0);
            }
            ;
        }
    }

    private void printSafe(String label, byte[] data) {
        System.out.print(label);
        if (data != null) {
            for (byte b : data) {
                System.out.print((char) b);
            }
        }
        System.out.println();
    }

}
