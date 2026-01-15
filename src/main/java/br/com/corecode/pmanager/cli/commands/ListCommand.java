package br.com.corecode.pmanager.cli.commands;

import br.com.corecode.pmanager.cli.Command;
import br.com.corecode.pmanager.cli.CommandContext;
import br.com.corecode.pmanager.session.VaultSession;

public class ListCommand implements Command {

    @Override
    public String name() {
        return "list";
    }

    @Override
    public void execute(CommandContext context) {
        try {
            VaultSession session = context.session();

            if (session.getVault().isEmpty()) {
                System.out.println("Cofre vazio.");
                return;
            }

            System.out.println("Credenciais salvas:");
            
            session.getVault().list().forEach(entry ->
                System.out.println("- " + entry.getId())
            );

        } catch (Exception e) {
            System.out.println("Erro ao listar credenciais: " + e.getMessage());
        }
    }

}
