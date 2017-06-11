package models;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


abstract public class UserOnline {
    private static Map<String, String> onlineSet = new HashMap<>();

    public static String signIn(String login, String password) {
        String token = null;
        if( UserDB.isValid(login, password) ) {
            token = Encryption.encryptString(login + new Date());
            onlineSet.put(token, login);
        }
        return token;
    }

    public static void signOut(String token) {
       onlineSet.remove(token);
    }

    public static String getLogin(String token) {
        return onlineSet.get(token);
    }

    public static boolean isValid(String token) {
        return token != null && onlineSet.containsKey(token);
    }
}
