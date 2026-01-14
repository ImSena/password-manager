package br.com.corecode.pmanager.cli.commands;

import java.io.Console;

import br.com.corecode.pmanager.cli.Command;
import br.com.corecode.pmanager.cli.CommandContext;
import br.com.corecode.pmanager.storage.VaultHandle;

public class UnlockCommand implements Command {
    @Override
    public String name() {
        return "unlock";
    }

    @Override
    public void execute(CommandContext context) {

        if (context.session().isUnlocked()) {
            System.out.println("Cofre já está desbloqueado.");
            return;
        }

        Console console = System.console();
        char[] password = console.readPassword("Senha mestra: ");

        try {
            VaultHandle handle = context.repository()
                    .open(context.vaultPath(), password);

            context.session().unlock(handle.key(), handle.vault(), handle.salt());

            System.out.println("Cofre desbloqueado.");

        } finally {
            java.util.Arrays.fill(password, '\0');
        }
    }
}
