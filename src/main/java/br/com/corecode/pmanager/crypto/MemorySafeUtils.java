package br.com.corecode.pmanager.crypto;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public final class MemorySafeUtils {

    private MemorySafeUtils() {
    }

    public static byte[] toBytes(char[] chars){
        CharBuffer charBuffer = CharBuffer.wrap(chars);
        ByteBuffer byteBuffer = StandardCharsets.UTF_8.encode(charBuffer);

        byte[] bytes = Arrays.copyOfRange(byteBuffer.array(),
            byteBuffer.position(), byteBuffer.limit()
        );

        Arrays.fill(byteBuffer.array(), (byte) 0);

        return bytes;
    }

}
