package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import models.users.UserOnline;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.login;

import javax.inject.Inject;


public class LoginController extends Controller {
    private final UserOnline userOnline;

    @Inject
    public LoginController(UserOnline userOnline) {
        this.userOnline = userOnline;
    }

    public Result index() {
        return userOnline.isValid( session().get("token") ) ?
                redirect(
                        routes.MainController.index()
                )
                : ok( login.render() );
    }

    public Result postLogin() {
        JsonNode req = request().body().asJson();
        String action = req.get("action").asText();

        switch(action) {
            case "authorize":
                return authorize();
            case "unauthorize":
                return unauthorize();
            default:
                return badRequest(
                        Json.newObject()
                                .put("error", "Неверное действие!")
                );
        }
    }

    private Result authorize() {
        JsonNode req = request().body().asJson();
        String login = req.get("login").asText();
        String password = req.get("password").asText();
        String token;

        token = userOnline.signIn(login, password);
        if(token == null) {
            return badRequest(
                    Json.newObject()
                            .put("error", "Неверное имя пользователя или пароль!")
            );
        }

        session().clear();
        session().put("token", token);

        return ok();
    }

    private Result unauthorize() {
        userOnline.signOut(
                session().get("token")
        );
        session().clear();

        return ok( login.render() );
    }
}
