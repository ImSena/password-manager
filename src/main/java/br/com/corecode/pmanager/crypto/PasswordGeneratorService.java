package br.com.corecode.pmanager.crypto;

public final class PasswordGeneratorService {
    private static final char[] ALPHABET = 
        "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()-_=+[]{}<>?".toCharArray();
    
    private static final int MIN_LENGTH = 5;
    
    private PasswordGeneratorService(){}

    public static char[] generate(int length){
        if(length <= MIN_LENGTH) throw new IllegalArgumentException("Tamanho deve ser maior que " + MIN_LENGTH);

        char[] password = new char[length];

        for(int i = 0; i < length; i++){
            int randomIndex = SecureRandomService.nextInt(ALPHABET.length);

            password[i] = ALPHABET[randomIndex];
        }

        return password;
    }
}
