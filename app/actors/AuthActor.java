package actors;

import akka.actor.Props;
import akka.actor.UntypedActor;
import models.notifications.Notification;
import models.shared.Encryption;
import models.users.UsersOnline;
import models.users.UserRepository;

import java.util.Date;


public class AuthActor extends UntypedActor {
    private final UserRepository userRepository;
    private final UsersOnline usersOnline;
    private final Notification notification;

    public AuthActor(UserRepository userRepository, UsersOnline usersOnline, Notification notification) {
        this.userRepository = userRepository;
        this.usersOnline = usersOnline;
        this.notification = notification;
    }

    public static Props props(UserRepository userRepository, UsersOnline usersOnline, Notification notification) {
        return Props.create(actors.AuthActor.class, userRepository, usersOnline, notification);
    }

    public void onReceive(Object msg) throws Exception {
        if(msg instanceof AuthActorProtocol.SignIn) {
            receiveSignIn((AuthActorProtocol.SignIn) msg);
        } else if(msg instanceof AuthActorProtocol.SignOut) {
            receiveSignOut((AuthActorProtocol.SignOut) msg);
        } else {
            unhandled(msg);
        }
    }

    private void receiveSignIn(AuthActorProtocol.SignIn msg) {
        String login = msg.login;
        String password = msg.password;
        String token = "";

        login = login.toLowerCase();
        if( userRepository.isValid(login, password) ) {
            token = Encryption.encryptString(login + new Date());
            usersOnline.add(token, login);
            notification.signIn(login);
        }

        sender().tell(token, self());
    }

    private void receiveSignOut(AuthActorProtocol.SignOut msg) {
        String token = msg.token;

        if( usersOnline.isValid(token) ) {
            notification.signOut( usersOnline.getLogin(token) );
            usersOnline.remove(token);
        }
    }
}