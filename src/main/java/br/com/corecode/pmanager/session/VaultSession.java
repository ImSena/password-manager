package br.com.corecode.pmanager.session;

import java.util.Arrays;

import javax.crypto.SecretKey;

import br.com.corecode.pmanager.domain.Vault;

public class VaultSession {
    private SecretKey key;
    private Vault vault;
    private byte[] salt;

    public boolean isUnlocked(){
        return key != null && vault != null;
    }

    public void unlock(SecretKey key, Vault vault, byte[] salt){
        this.key = key;
        this.vault = vault;
        this.salt = salt;
    }

    public Vault getVault(){
        if(!isUnlocked()){
            throw new IllegalStateException("Cofre está bloqueado");
        }

        return vault;
    }

    public SecretKey getKey(){
        if(!isUnlocked()){
            throw new IllegalStateException("Cofre está bloqueado");
        }

        return key;
    }

    public void lock(){
        key = null;
        vault = null;

        if (salt != null) {
            Arrays.fill(salt, (byte) 0);
            salt = null;
        }
    }

    public byte[] getSalt(){
        return salt;
    }

    private void ensureUnlocked() {
        if (!isUnlocked()) {
            throw new IllegalStateException("Cofre está bloqueado");
        }
    }
}
