package models.users;

import models.shared.Encryption;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@Singleton
public class UserOnline {
    private Map<String, String> onlineSet = new HashMap<>();
    private final UserRepository userRepository;

    @Inject
    public UserOnline(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public String signIn(String login, String password) {
        String token = null;
        if( userRepository.isValid(login, password) ) {
            token = Encryption.encryptString(login + new Date());
            onlineSet.put(token, login);
        }
        return token;
    }

    public void signOut(String token) {
       onlineSet.remove(token);
    }

    public String getLogin(String token) {
        return onlineSet.get(token);
    }

    public boolean isValid(String token) {
        return token != null && onlineSet.containsKey(token);
    }
}