package models;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;


public class UserOnline {
    private static Set<String> onlineSet= new HashSet<>();

    public static String signIn(String login, String password) {
        String token = null;
        if( UserDB.isValid(login, password) ) {
            token = login + new Date();
            onlineSet.add(token);
        }
        return token;
    }

    public static void signOut(String token) {
       onlineSet.remove(token);
    }

    public static boolean isValid(String token) {
        return token != null && onlineSet.contains(token);
    }
}
