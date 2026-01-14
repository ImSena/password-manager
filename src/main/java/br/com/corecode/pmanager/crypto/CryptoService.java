package br.com.corecode.pmanager.crypto;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;

public final class CryptoService {
    private CryptoService(){}

    public static byte[] encrypt(byte[] plainBytes, SecretKey key, byte[] iv){
        try{

            Cipher cipher = Cipher.getInstance(CryptoConfig.CIPHER_ALGORITHM);

            GCMParameterSpec spec = new GCMParameterSpec(CryptoConfig.GCM_TAG_LENGTH, iv);

            cipher.init(Cipher.ENCRYPT_MODE, key, spec);

            return cipher.doFinal(plainBytes);

        }catch(Exception e){
            throw new RuntimeException("Erro ao criptografar dados: ", e);
        }
    }

    public static byte[] decrypt(byte[] cipherBytes, SecretKey key, byte[] iv){
        try{
            Cipher cipher = Cipher.getInstance(CryptoConfig.CIPHER_ALGORITHM);

            GCMParameterSpec spec = new GCMParameterSpec(CryptoConfig.GCM_TAG_LENGTH, iv);

            cipher.init(Cipher.DECRYPT_MODE, key, spec);

            return cipher.doFinal(cipherBytes);
        }catch(Exception e){
            throw new RuntimeException("Erro ao descriptografar dados: ", e);
        }
    }
}
