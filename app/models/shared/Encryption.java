package models.shared;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


abstract public class Encryption {
    public static String encryptString(String string) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(string.getBytes());
            byte[] digest = messageDigest.digest();

            StringBuffer enstr = new StringBuffer();
            for (byte b : digest) {
                enstr.append(String.format("%02x", b & 0xff));
            }
            string = enstr.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return string;
    }
}
