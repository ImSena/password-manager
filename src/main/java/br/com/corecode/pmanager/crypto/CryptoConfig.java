package br.com.corecode.pmanager.crypto;

public final class CryptoConfig {
    private CryptoConfig(){}

    public static final String KDF_ALGORITHM = "PBKDF2WithHmacSHA256";
    public static final int KDF_INTERATIONS = 120_000;
    public static final int KEY_LENGTH = 256;

    public static final String CIPHER_ALGORITHM = "AES/GCM/NoPadding";
    public static final int GCM_TAG_LENGTH = 128;

    public static final int SALT_LENGTH = 16;
    public static final int IV_LENGTH = 12;

    public static final byte FILE_VERSION = 0x01;

}
