package actors;

import akka.actor.Props;
import akka.actor.UntypedActor;
import models.users.UserRepository;


@Deprecated
public class AuthActor extends UntypedActor {
    private final UserRepository userRepository;

    public AuthActor(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public static Props props(UserRepository userRepository) {
        return Props.create(actors.AuthActor.class, userRepository);
    }

    public void onReceive(Object msg) throws Exception {

    }

    private void receiveSignIn(AuthActorProtocol msg) {

    }

    private void receiveSignOut(AuthActorProtocol msg) {

    }
}