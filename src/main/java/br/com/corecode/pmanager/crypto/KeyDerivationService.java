package br.com.corecode.pmanager.crypto;

import java.security.spec.KeySpec;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public final class KeyDerivationService {

    private KeyDerivationService(){}

    public static SecretKey deriveKey(char[] masterPassword, byte[] salt){
        try{
            SecretKeyFactory factory = SecretKeyFactory.getInstance(CryptoConfig.KDF_ALGORITHM);

            KeySpec spec = new PBEKeySpec(
                masterPassword,
                salt,
                CryptoConfig.KDF_INTERATIONS,
                CryptoConfig.KEY_LENGTH
            );

            SecretKey tmp = factory.generateSecret(spec);

            return new SecretKeySpec(tmp.getEncoded(), "AES");
        }catch(Exception e){
            throw new RuntimeException("Erro ao derivar chave criptográfica");
        }
    }
}
