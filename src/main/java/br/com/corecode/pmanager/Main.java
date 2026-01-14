package br.com.corecode.pmanager;

import java.nio.file.Path;
import java.util.Scanner;

import br.com.corecode.pmanager.cli.CommandContext;
import br.com.corecode.pmanager.cli.CommandDispatcher;
import br.com.corecode.pmanager.cli.Shell;
import br.com.corecode.pmanager.cli.commands.AddCommand;
import br.com.corecode.pmanager.cli.commands.GetCommand;
import br.com.corecode.pmanager.cli.commands.InitCommand;
import br.com.corecode.pmanager.cli.commands.ListCommand;
import br.com.corecode.pmanager.cli.commands.RemoveCommand;
import br.com.corecode.pmanager.storage.VaultFileRepository;

public class Main {
    public static void main(String[] args) {
        
        Scanner scanner = new Scanner(System.in);
        VaultFileRepository repository = new VaultFileRepository();
        Path vaultPath = Path.of("vault.pmv");

        CommandContext context = new CommandContext(scanner, repository, vaultPath, args);

        CommandDispatcher dispatcher = new CommandDispatcher();

        dispatcher.register(new InitCommand());
        dispatcher.register(new AddCommand());
        dispatcher.register(new ListCommand());
        dispatcher.register(new GetCommand());
        dispatcher.register(new RemoveCommand());

        Shell shell = new Shell(dispatcher, context, scanner);
        shell.start();
    }
}
