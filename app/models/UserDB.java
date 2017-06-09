package models;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;


public class UserDB {
    private static Map<String, String> users = new HashMap<>();

    public static void addUser(String login, String password) {
        users.put(login, encryptPassword(password));
    }

    public static boolean isUser(String login) {
        return users.containsKey(login);
    }

    private static String encryptPassword(String password) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(password.getBytes());
            byte[] digest = messageDigest.digest();

            StringBuffer enpass = new StringBuffer();
            for (byte b : digest) {
                enpass.append(String.format("%02x", b & 0xff));
            }
            password = enpass.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return password;
    }

    static boolean isValid(String login, String password) {
        return ((login != null)
                &&
                (password != null)
                &&
                isUser(login)
                &&
                users.get(login).equals( encryptPassword(password) ));
    }
}
