package br.com.corecode.pmanager.domain;

import java.util.Objects;

public class PasswordEntry {
    private final String id;
    private final String username;
    private final String password;
    private final String notes;

    public PasswordEntry(String id, String username, String password, String notes){
        if(id == null || id.isBlank()){
            throw new IllegalArgumentException("Id não pode ser vazio");
        }

        this.id = id;
        this.username = username;
        this.password = password;
        this.notes = notes;
    }

    public PasswordEntry(){
        this.id = null;
        this.username = null;
        this.password = null;
        this.notes = null;
    }

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getNotes() {
        return notes;
    }

    @Override 
    public boolean equals(Object obj){
        if (this == obj) return true;

        if(!(obj instanceof PasswordEntry that)) return false;

        return id.equals(that.id);
    }

    @Override
    public int hashCode(){
        return Objects.hash(id);
    }

}
