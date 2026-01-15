package br.com.corecode.pmanager.cli;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public final class SecureInput {
    private SecureInput(){}

    public static char[] readChars(String prompt){
        System.out.print(prompt);

        try{
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            int c;

            while ((c = System.in.read()) != -1){
                if(c == '\n') break;
                if(c == '\r') continue;
                buffer.write(c);
            }

            return buffer.toString().toCharArray();
        }catch(IOException e){
            throw new RuntimeException("Erro ao ler entrada", e);
        }
    }
}
