package br.com.corecode.pmanager.cli;

import java.util.HashMap;
import java.util.Map;

public class CommandDispatcher {
    private final Map<String, Command> commands = new HashMap<>();

    public void register(Command command){
        commands.put(command.name(), command);
    }

    public void dispatch(String commandName, CommandContext context){
        Command command = commands.get(commandName);

        if(command == null){
            System.out.println("Comando inválido: " + commandName);
            return;
        }

        command.execute(context);
    }
}
