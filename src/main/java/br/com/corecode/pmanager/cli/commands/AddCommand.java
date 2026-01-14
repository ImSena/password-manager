package br.com.corecode.pmanager.cli.commands;

import java.nio.file.Files;

import br.com.corecode.pmanager.cli.Command;
import br.com.corecode.pmanager.cli.CommandContext;
import br.com.corecode.pmanager.domain.PasswordEntry;
import br.com.corecode.pmanager.session.VaultSession;

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

            VaultSession session = context.session();

            if(!session.isUnlocked()){
                System.out.println("Cofre bloqueado. Execute 'unlock' primeiro.");
                return;
            }

            System.out.println("ID: ");
            String id = context.scanner().nextLine();

            System.out.println("Usuário: ");
            String user = context.scanner().nextLine();

            System.out.println("Senha");
            String password = context.scanner().nextLine();

            System.out.println("Descrição (opcional): ");
            String description = context.scanner().nextLine();

            session.getVault().add(new PasswordEntry(id, user, password, description));


            context.repository().save(context.vaultPath(), session.getVault(), session.getKey(), session.getSalt());

            System.out.println("Credencial adicionada com sucesso. ");

        }catch(Exception e){
            System.out.println("Erro ao adicionar credencial: "+e.getMessage());
        }
    }

}
