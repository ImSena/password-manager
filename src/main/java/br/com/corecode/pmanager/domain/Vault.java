package br.com.corecode.pmanager.domain;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Vault {
    private final Map<String, PasswordEntry> entries = new HashMap<>();

    public Vault(){}

    public void add(PasswordEntry entry){
        if(entries.containsKey(entry.getId())){
            throw new IllegalArgumentException("Já existe uma entrada com esse ID");
        }

        entries.put(entry.getId(), entry);
    }

    public PasswordEntry get(String id){
        return entries.get(id);
    }

    public void remove(String id){
        entries.remove(id);
    }

    public Collection<PasswordEntry> list(){
        return Collections.unmodifiableCollection(entries.values());
    }
  
    public Map<String, PasswordEntry> getEntries(){
        return entries;
    }

    @JsonIgnore
    public boolean isEmpty(){
        return entries.isEmpty();
    }
}
