package br.com.corecode.pmanager.cli.commands;

import java.io.Console;
import java.nio.file.Files;
import java.util.Arrays;

import br.com.corecode.pmanager.cli.Command;
import br.com.corecode.pmanager.cli.CommandContext;

public class InitCommand implements Command {
    @Override
    public String name() {
        return "init";
    }

    @Override
    public void execute(CommandContext context) {
        Console console = System.console();
        try {
            if (Files.exists(context.vaultPath())) {
                System.out.println("Cofre já existe");
                return;
            }

            char[] p1 = console.readPassword("Crie a senha mestra: ");
            char[] p2 = console.readPassword("Confirme a senha mestra: ");

            if (!Arrays.equals(p1, p2)) {
                System.out.println("Senhas não conferem");
                Arrays.fill(p1, '\0');
                Arrays.fill(p2, '\0');
                execute(context);
                return;
            }

            context.repository().create(context.vaultPath(), p1);

            System.out.println("Cofre criado com sucesso.");

            Arrays.fill(p1, '\0');
            Arrays.fill(p2, '\0');
        } catch (Exception e) {
            System.out.println("Erro ao criar cofre: " + e.getMessage());
        }

    }

    @Override
    public boolean requiresAuth() {
        return false;
    }

}
