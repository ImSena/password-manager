package br.com.corecode.pmanager.cli.commands;

import br.com.corecode.pmanager.cli.Command;
import br.com.corecode.pmanager.cli.CommandContext;
import br.com.corecode.pmanager.domain.PasswordEntry;
import br.com.corecode.pmanager.session.VaultSession;

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

            VaultSession session = context.session();

            String id = context.args()[1];
            PasswordEntry entry = session.getVault().get(id);

            if (entry == null) {
                System.out.println("Credencial não encontrada.");
                return;
            }

            System.out.print("Confirma remoção de '" + id + "'? (s/n): ");
            String confirm = context.scanner().nextLine();

            if (!"s".equalsIgnoreCase(confirm)) {
                System.out.println("Operação cancelada.");
                return;
            }

            session.getVault().remove(id);

            context.repository().save(
                context.vaultPath(),
                session.getVault(),
                session.getKey(),
                session.getSalt()
            );

            System.out.println("Credencial removida com sucesso.");

        } catch (Exception e) {
            System.out.println("Erro ao remover credencial: " + e.getMessage());
        }

    }

}
