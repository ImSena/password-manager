package br.com.corecode.pmanager.cli;

import java.util.Scanner;

public class Shell {
    private final CommandDispatcher dispatcher;
    private final CommandContext context;
    private final Scanner scanner;

    public Shell(CommandDispatcher dispatcher, CommandContext context, Scanner scanner){
        this.dispatcher = dispatcher;
        this.context = context;
        this.scanner = scanner;
    }

    public void start(){
        System.out.println("Password Manager CLI");
        System.out.println("Digite 'help' para ver os comandos. ");
        System.out.println("Digite 'exit' para sair.\n");

        while(true){
            System.out.print("pmanager> ");
            String line = scanner.nextLine().trim();

            if(line.isEmpty()){
                continue;
            }

            if("exit".equalsIgnoreCase(line)){
                System.out.println("Bye");
                break;
            }

            dispatchLine(line);
        }
    }

    private void dispatchLine(String line){
        String[] parts = line.split("\\s+");
        dispatcher.dispatch(parts[0], context.withArgs(parts));
    }
}
