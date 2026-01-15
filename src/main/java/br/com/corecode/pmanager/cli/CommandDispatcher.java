package br.com.corecode.pmanager.cli;

import java.io.Console;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import br.com.corecode.pmanager.storage.VaultHandle;

public class CommandDispatcher {
    private final Map<String, Command> commands = new HashMap<>();

    public void register(Command command) {
        commands.put(command.name(), command);
    }

    public void dispatch(String commandName, CommandContext context) {
        Command command = commands.get(commandName);

        if (command == null) {
            System.out.println("Comando inválido: " + commandName);
            return;
        }

        if (command.requiresAuth() && !context.session().isUnlocked()) {
            boolean unlocked = performAutoUnlock(context);

            if (!unlocked) {
                System.out.println("Falha na autenticação. Comando cancelado.");
                return;
            }
        }

        command.execute(context);
    }

    private boolean performAutoUnlock(CommandContext context) {
        Console console = System.console();

        if (console == null) {
            System.out.println("Console não disponível para leitura segura de senha.");
            return false;
        }

        char[] password = console.readPassword("Senha Mestra: ");

        if (password == null) {
            System.out.println("\nOperação cancelada.");
            return false;
        }

        try {
            VaultHandle handle = context.repository()
                    .open(context.vaultPath(), password);

            context.session().unlock(handle.key(), handle.vault(), handle.salt());
            return true;

        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
            return false;
        } finally {
            if (password != null) {
                Arrays.fill(password, '\0');
            }
        }
    }
}
