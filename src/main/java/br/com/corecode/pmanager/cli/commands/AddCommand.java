package br.com.corecode.pmanager.cli.commands;

import java.nio.file.Files;
import java.util.Arrays;

import br.com.corecode.pmanager.cli.Command;
import br.com.corecode.pmanager.cli.CommandContext;
import br.com.corecode.pmanager.domain.PasswordEntry;
import br.com.corecode.pmanager.domain.Vault;

public class AddCommand implements Command{

    @Override
    public String name() {
        return "add";
    }

    @Override
    public void execute(CommandContext context) {
        try{
            if(!Files.exists(context.vaultPath())){
                System.out.println("Cofre não existe. Execute 'init' primeiro");
                return;
            }

            System.out.println("Senha mestra: ");
            char[] master = context.scanner().nextLine().toCharArray();

            Vault vault = context.repository().open(context.vaultPath(), master);

            System.out.println("ID: ");
            String id = context.scanner().nextLine();

            System.out.println("Usuário: ");
            String user = context.scanner().nextLine();

            System.out.println("Senha");
            String password = context.scanner().nextLine();

            System.out.println("Descrição (opcional): ");
            String description = context.scanner().nextLine();

            vault.add(new PasswordEntry(id, user, password, description));

            context.repository().save(context.vaultPath(), vault, master);

            System.out.println("Credencial adicionada com sucesso. ");

            Arrays.fill(master, '\0');

        }catch(Exception e){
            System.out.println("Erro ao adicionar credencial: "+e.getMessage());
        }
    }

}
