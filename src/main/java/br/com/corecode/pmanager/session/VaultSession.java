package br.com.corecode.pmanager.session;

import java.util.Arrays;

import javax.crypto.SecretKey;

import br.com.corecode.pmanager.domain.Vault;

public class VaultSession {
    private static final long TIMEOUT_MILLIS = 1 * 60 * 1000;
    private SecretKey key;
    private Vault vault;
    private byte[] salt;
    private long lastAccess;

    public boolean isUnlocked() {
        if(key == null || vault == null){
            return false;
        }

        if(isExpired()){
            lock();
            return false;
        }

        return true;
    }

    public void unlock(SecretKey key, Vault vault, byte[] salt) {
        this.key = key;
        this.vault = vault;
        this.salt = salt;

        touch();
    }

    public Vault getVault() {
        if (!isUnlocked()) {
            throw new IllegalStateException("Cofre está bloqueado");
        }

        return vault;
    }

    public SecretKey getKey() {
        if (!isUnlocked()) {
            throw new IllegalStateException("Cofre está bloqueado");
        }

        return key;
    }

    public void lock() {
        key = null;
        vault = null;

        if (salt != null) {
            Arrays.fill(salt, (byte) 0);
            salt = null;
        }

        lastAccess = 0;
    }

    public byte[] getSalt() {
        return salt;
    }

    private void ensureUnlocked() {
        if (!isUnlocked()) {
            throw new IllegalStateException("Cofre está bloqueado");
        }
    }

    private void touch(){
        this.lastAccess = System.currentTimeMillis();
    }

    private boolean isExpired(){
        return System.currentTimeMillis() - lastAccess > TIMEOUT_MILLIS;
    }
}
