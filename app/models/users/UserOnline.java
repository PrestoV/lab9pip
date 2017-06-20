package models.users;

import models.notifications.Notification;
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
    private final Notification notification;

    @Inject
    public UserOnline(UserRepository userRepository, Notification notification) {
        this.userRepository = userRepository;
        this.notification = notification;

        userRepository.add(
                new User("presto", "555")
        );
        userRepository.add(
                new User("kek", "555")
        );
        userRepository.add(
                new User("lol", "555")
        );
    }

    public String signIn(String login, String password) {
        String token = null;
        login = login.toLowerCase();
        if( userRepository.isValid(login, password) ) {
            token = Encryption.encryptString(login + new Date());
            onlineSet.put(token, login);
            notification.signIn(login);
        }
        return token;
    }

    public void signOut(String token) {
        if( isValid(token) ) {
            notification.signOut(getLogin(token));
            onlineSet.remove(token);
        }
    }

    public String getLogin(String token) {
        return onlineSet.get(token);
    }

    public boolean isValid(String token) {
        return token != null && onlineSet.containsKey(token);
    }
}