package br.com.corecode.pmanager.crypto;

import javax.crypto.SecretKey;

public record DerivedKey(
    SecretKey key,
    byte[] salt
) {

}
