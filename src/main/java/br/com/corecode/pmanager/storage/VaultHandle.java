package br.com.corecode.pmanager.storage;

import javax.crypto.SecretKey;

import br.com.corecode.pmanager.domain.Vault;

public record VaultHandle(
        Vault vault,
        SecretKey key,
        byte[] salt
    ) {
}
