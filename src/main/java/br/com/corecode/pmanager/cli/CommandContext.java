package br.com.corecode.pmanager.cli;

import java.nio.file.Path;
import java.util.Scanner;

import br.com.corecode.pmanager.session.VaultSession;
import br.com.corecode.pmanager.storage.VaultFileRepository;

public class CommandContext {
    private final Scanner scanner;
    private final VaultFileRepository repository;
    private final Path vaultPath;
    private final String[] args;
    private final VaultSession session;

    public CommandContext(
        Scanner scanner, 
        VaultFileRepository repository, 
        Path vaultPath, 
        String[] args, 
        VaultSession session
    ){
        this.scanner = scanner;
        this.repository = repository;
        this.vaultPath = vaultPath;
        this.args = args;
        this.session = session;
    }

    public CommandContext withArgs(String[] args){
        return new CommandContext(scanner, repository, vaultPath, args, session);
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

    public VaultSession session(){
        return session;
    }
}
