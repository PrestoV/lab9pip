package models;

import java.util.HashMap;
import java.util.Map;


abstract public class UserDB {
    private static Map<String, String> users = new HashMap<>();

    public static void addUser(String login, String password) {
        users.put(login, Encryption.encryptString(password));
    }

    public static boolean isUser(String login) {
        return users.containsKey(login);
    }

    static boolean isValid(String login, String password) {
        return ((login != null)
                &&
                (password != null)
                &&
                isUser(login)
                &&
                users.get(login).equals( Encryption.encryptString(password) ));
    }
}
