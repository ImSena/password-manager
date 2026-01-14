package br.com.corecode.pmanager.cli.commands;

import java.nio.file.Files;
import java.util.Arrays;

import br.com.corecode.pmanager.cli.Command;
import br.com.corecode.pmanager.cli.CommandContext;
import br.com.corecode.pmanager.domain.PasswordEntry;
import br.com.corecode.pmanager.domain.Vault;

public class GetCommand implements Command {

    @Override
    public String name() {
        return "get";
    }

    @Override
    public void execute(CommandContext context) {
        try {
            if (context.args().length < 2) {
                System.out.println("Uso: pmanager get <id>");
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

            System.out.println("ID: " + entry.getId());
            System.out.println("Usuário: " + entry.getUsername());
            System.out.println("Senha: " + entry.getPassword());
            System.out.println("Descrição: " + entry.getNotes());

            Arrays.fill(master, '\0');

        } catch (Exception e) {
            System.out.println("Erro ao obter credencial: " + e.getMessage());
        }
    }

}
