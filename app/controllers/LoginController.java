package controllers;

import actors.AuthActor;
import actors.AuthActorProtocol;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import com.fasterxml.jackson.databind.JsonNode;
import models.notifications.Notification;
import models.users.UserRepository;
import models.users.UsersOnline;
import play.libs.Json;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Controller;
import play.mvc.Result;
import scala.compat.java8.FutureConverters;
import views.html.login;

import javax.inject.Inject;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import static akka.pattern.Patterns.ask;


public class LoginController extends Controller {
    private final HttpExecutionContext executionContext;
    private final ActorRef authProducer;
    private final UsersOnline usersOnline;

    @Inject
    public LoginController(ActorSystem actorSystem, UserRepository userRepository, UsersOnline usersOnline,
                           Notification notification, HttpExecutionContext executionContext) {
        this.authProducer = actorSystem.actorOf( AuthActor.props(userRepository, usersOnline, notification) );
        this.usersOnline = usersOnline;
        this.executionContext = executionContext;

        /*
         * TODO: FOR TEST, REMOVE IT
         */
        if( !userRepository.isValid("presto", "555") ) {
            userRepository.add(
                    new models.users.User(
                            "presto",
                            "555"
                    )
            );
        }
    }

    public Result index() {
        return usersOnline.isValid( session().get("token") ) ?
                redirect(
                        routes.MainController.index()
                )
                : ok( login.render() );
    }

    public CompletionStage<Result> postLogin() {
        JsonNode req = request().body().asJson();
        String action = req.get("action").asText();

        switch(action) {
            case "authorize":
                return authorize();
            case "unauthorize":
                return unauthorize();
            default:
                return CompletableFuture.completedFuture(
                        badRequest(
                                Json.newObject()
                                        .put("error", "Неверное действие!")
                        )
                );
        }
    }

    private CompletionStage<Result> authorize() {
        JsonNode req = request().body().asJson();
        String login = req.get("login").asText();
        String password = req.get("password").asText();

        return FutureConverters.toJava(
                ask(authProducer,
                        new AuthActorProtocol.SignIn(login, password),
                        1000)
        ).thenApplyAsync(
                token -> {
                    if( ((String)token).length() == 0) {
                        return badRequest(
                                Json.newObject()
                                        .put("error", "Неверное имя пользователя или пароль!")
                        );
                    }
                    session().clear();
                    session().put("token", (String)token);
                    return ok();
                }, executionContext.current()
        );
    }

    private CompletionStage<Result> unauthorize() {
        authProducer.tell(
                new AuthActorProtocol.SignOut( session().get("token") ),
                ActorRef.noSender()
        );
        session().clear();
        return CompletableFuture.completedFuture(
                ok( login.render() )
        );
    }
}
