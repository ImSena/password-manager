package br.com.corecode.pmanager.domain;

import java.util.Arrays;
import java.util.Objects;

public class PasswordEntry {
    private String id;
    private byte[] encryptedData;
    private byte[] iv;

    public PasswordEntry(String id, byte[] encryptedData, byte[] iv) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("Id não pode ser vazio");
        }

        this.id = id;
        this.encryptedData = encryptedData;
        this.iv = iv;
    }

    public PasswordEntry() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public byte[] getEncryptedData() {
        return encryptedData;
    }

    public void setEncryptedData(byte[] encryptedData) {
        this.encryptedData = encryptedData;
    }

    public byte[] getIv() {
        return iv;
    }

    public void setIv(byte[] iv) {
        this.iv = iv;
    }

    public void clear() {
        if (encryptedData != null) {
            Arrays.fill(encryptedData, (byte) 0);
        }

        if (iv != null)
            Arrays.fill(iv, (byte) 0);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;

        if (!(obj instanceof PasswordEntry that)) return false;

        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
