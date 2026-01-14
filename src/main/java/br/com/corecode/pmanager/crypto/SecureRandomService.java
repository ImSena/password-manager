package br.com.corecode.pmanager.crypto;

import java.security.SecureRandom;

public final class SecureRandomService {

    private static final SecureRandom RANDOM = new SecureRandom();

    private SecureRandomService(){}

    public static byte[] generateBytes(int length){
        byte[] bytes = new byte[length];
        RANDOM.nextBytes(bytes);
        return bytes;
    }
}
