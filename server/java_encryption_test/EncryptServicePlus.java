


import static java.nio.charset.StandardCharsets.US_ASCII;
import static java.nio.charset.StandardCharsets.UTF_8;

import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class EncryptServicePlus{

    private static final String SALTED_STR = "Salted__";

    private static final byte[] SALTED_MAGIC = SALTED_STR.getBytes(US_ASCII);


    public static void main(String[] args) throws Exception{

        String content = "{\"input\":{\"serviceId\":\"1724\",\"appForm\":{\"official_address\":\"12312321312312\",\"official_pincode\":\"123123213\",\"application_dist\":\"123123213\",\"application_type\":\"123123213\",\"applicant_for\":\"123123213\",\"applicant_email_id\":\"123123213\",\"applicant_mobile_no\":\"123123213\",\"applicant_mobile_no\":\\\\\\\"123123213\\\\\\\"}}}";
       
        String authKey = "EAl2056iXl743JAll6464moR";

        String encryptedDetails = encryptExternal(content,authKey);

        System.out.println(encryptedDetails);

        return;

    }

    private static String encryptExternal(String jsonData, String token) throws Exception {
            
        final byte[] pass = token.getBytes(US_ASCII); 
        final byte[] salt = (new SecureRandom()).generateSeed(8); 
        final byte[] inBytes = jsonData.getBytes(UTF_8); 

        final byte[] passAndSalt = array_concat(pass, salt); 
        byte[] hash = new byte[0];
        byte[] keyAndIv = new byte[0];

        for (int i = 0; i < 3 && keyAndIv.length < 48; i++) { 

            final byte[] hashData = array_concat(hash, passAndSalt);
            final MessageDigest md = MessageDigest.getInstance("MD5");
            hash = md.digest(hashData);
            keyAndIv = array_concat(keyAndIv, hash);
            final byte[] keyValue = Arrays.copyOfRange(keyAndIv, 0,16);
            final byte[] iv = Arrays.copyOfRange(keyAndIv, 16, 32); 
            final SecretKeySpec key = new SecretKeySpec(keyValue,"AES");
            final Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(iv));
            byte[] data = cipher.doFinal(inBytes);
            data  = array_concat(array_concat(SALTED_MAGIC, salt),data);
            return Base64.getEncoder().encodeToString(data);

        }
        return "data return"; 
        
    }

    public static byte[] array_concat(final byte[]a, final byte[]b){
        final byte[] c = new byte[a.length + b.length];
        System.arraycopy(a, 0, c, 0, a.length);
        System.arraycopy(b, 0, c, a.length, b.length);
        return c;
    }

}
