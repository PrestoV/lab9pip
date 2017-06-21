package models.users;

import javax.inject.Singleton;
import java.util.HashMap;
import java.util.Map;


@Singleton
public class UsersOnline {
    private Map<String, String> onlineSet = new HashMap<>();

    public void add(String token, String login) {
        onlineSet.put(token, login);
    }

    public void remove(String token) {
        onlineSet.remove(token);
    }

    public String getLogin(String token) {
        return onlineSet.get(token);
    }

    public boolean isValid(String token) {
        return token != null && onlineSet.containsKey(token);
    }
}