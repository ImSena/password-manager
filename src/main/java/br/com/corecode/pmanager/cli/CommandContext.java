package br.com.corecode.pmanager.cli;

import java.nio.file.Path;
import java.util.Scanner;

import br.com.corecode.pmanager.storage.VaultFileRepository;

public class CommandContext {
    private final Scanner scanner;
    private final VaultFileRepository repository;
    private final Path vaultPath;
    private final String[] args;

    public CommandContext(Scanner scanner, VaultFileRepository repository, Path vaultPath, String[] args){
        this.scanner = scanner;
        this.repository = repository;
        this.vaultPath = vaultPath;
        this.args = args;
    }

    public CommandContext withArgs(String[] args){
        return new CommandContext(scanner, repository, vaultPath, args);
    }

    public Scanner scanner(){
        return scanner;
    }

    public VaultFileRepository repository(){
        return repository;
    }

    public Path vaultPath(){
        return vaultPath;
    }

    public String[] args(){
        return args;
    }
}
