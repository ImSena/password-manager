package br.com.corecode.pmanager.cli;

import java.util.Scanner;

public class Shell {
    private final CommandDispatcher dispatcher;
    private final CommandContext context;
    private final Scanner scanner;

    public Shell(CommandDispatcher dispatcher, CommandContext context, Scanner scanner) {
        this.dispatcher = dispatcher;
        this.context = context;
        this.scanner = scanner;
    }

    public void start() {
        System.out.println("Password Manager CLI");
        System.out.println("Digite 'help' para ver os comandos. ");
        System.out.println("Digite 'exit' para sair.\n");

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("\nFinalizando pmanager...");
        }));

        while (true) {
            System.out.print("pmanager> ");

            if (!scanner.hasNextLine()) {
                break;
            }

            String line;

            try {
                line = scanner.nextLine().trim();
            } catch (Exception e) {
                break;
            }

            if (line.isEmpty()) {
                continue;
            }

            if ("exit".equalsIgnoreCase(line)) {
                System.out.println("Bye");
                System.exit(0);
            }

            try {
                dispatchLine(line);
            } catch (Exception e) {
                System.out.println("Erro inesperando no comando: " + e.getMessage());
            }
        }
    }

    private void dispatchLine(String line) {
        String[] parts = line.split("\\s+");

        if (parts.length > 0 && !parts[0].isEmpty()) {
            dispatcher.dispatch(parts[0], context.withArgs(parts));
        }
    }
}
