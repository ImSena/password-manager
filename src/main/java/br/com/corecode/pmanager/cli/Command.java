package br.com.corecode.pmanager.cli;

public interface Command {
    String name();
    void execute(CommandContext context);
}
