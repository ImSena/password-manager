package br.com.corecode.pmanager.cli.commands;

import java.nio.file.Files;
import java.util.Arrays;

import br.com.corecode.pmanager.cli.Command;
import br.com.corecode.pmanager.cli.CommandContext;
import br.com.corecode.pmanager.domain.PasswordEntry;
import br.com.corecode.pmanager.domain.Vault;

public class RemoveCommand implements Command {

    @Override
    public String name() {
        return "remove";
    }

    @Override
    public void execute(CommandContext context) {
        try {
            if (context.args().length < 2) {
                System.out.println("Uso: pmanager remove <id>");
                return;
            }

            if (!Files.exists(context.vaultPath())) {
                System.out.println("Cofre não existe.");
                return;
            }

            String id = context.args()[1];

            System.out.print("Senha mestra: ");
            char[] master = context.scanner().nextLine().toCharArray();

            Vault vault = context.repository()
                    .open(context.vaultPath(), master);

            PasswordEntry entry = vault.get(id);
            if (entry == null) {
                System.out.println("Credencial não encontrada: " + id);
                return;
            }

            System.out.print("Confirma remoção de '" + id + "'? (sim/não): ");
            String confirm = context.scanner().nextLine();

            if (!"sim".equalsIgnoreCase(confirm)) {
                System.out.println("Operação cancelada.");
                return;
            }

            vault.remove(id);

            context.repository()
                    .save(context.vaultPath(), vault, master);

            System.out.println("Credencial removida com sucesso.");

            Arrays.fill(master, '\0');

        } catch (Exception e) {
            System.out.println("Erro ao remover credencial: " + e.getMessage());
        }

    }

}
