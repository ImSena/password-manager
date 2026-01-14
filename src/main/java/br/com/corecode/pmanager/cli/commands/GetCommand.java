package br.com.corecode.pmanager.cli.commands;

import java.nio.file.Files;
import java.util.Arrays;

import br.com.corecode.pmanager.cli.Command;
import br.com.corecode.pmanager.cli.CommandContext;
import br.com.corecode.pmanager.domain.PasswordEntry;
import br.com.corecode.pmanager.domain.Vault;
import br.com.corecode.pmanager.session.VaultSession;
import br.com.corecode.pmanager.storage.VaultHandle;

public class GetCommand implements Command {

    @Override
    public String name() {
        return "get";
    }

    @Override
    public void execute(CommandContext context) {
        try {
            if (context.args().length < 2) {
                System.out.println("Uso: pmanager get <id>");
                return;
            }

            if (!Files.exists(context.vaultPath())) {
                System.out.println("Cofre não existe.");
                return;
            }

            VaultSession session = context.session();

            if(!session.isUnlocked()){
                System.out.println("Cofre bloqueado. Execute 'unlock' primeiro.");
                return;
            }

            String id = context.args()[1];

            PasswordEntry entry = session.getVault().get(id);
            
            if (entry == null) {
                System.out.println("Credencial não encontrada: " + id);
                return;
            }

            System.out.println("ID: " + entry.getId());
            System.out.println("Usuário: " + entry.getUsername());
            System.out.println("Senha: " + entry.getPassword());
            System.out.println("Descrição: " + entry.getNotes());

        } catch (Exception e) {
            System.out.println("Erro ao obter credencial: " + e.getMessage());
        }
    }

}
