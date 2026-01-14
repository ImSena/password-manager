package br.com.corecode.pmanager.cli.commands;

import java.nio.file.Files;
import java.util.Arrays;

import br.com.corecode.pmanager.cli.Command;
import br.com.corecode.pmanager.cli.CommandContext;
import br.com.corecode.pmanager.domain.Vault;

public class ListCommand implements Command{

    @Override
    public String name() {
        return "list";
    }

    @Override
    public void execute(CommandContext context) {
        try{
            if(!Files.exists(context.vaultPath())){
                System.out.println("Cofre não existe");
                return;
            }

            System.out.println("Senha mestra: ");

            char[] master = context.scanner().nextLine().toCharArray();

            Vault vault = context.repository().open(context.vaultPath(), master);

            if(vault.isEmpty()){
                System.out.println("Cofre vazio.");
                return;
            }

            System.out.println("Credenciais salvas: ");
            vault.list().forEach(entry -> 
                System.out.println("- "+ entry.getId())
            );

            Arrays.fill(master, '\0');

        }catch(Exception e){
            System.out.println("Erro ao listar credenciais: " + e.getMessage());
        }
    }

}
