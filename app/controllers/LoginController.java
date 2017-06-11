package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.UserDB;
import models.UserOnline;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.login;


public class LoginController extends Controller {
    public Result index() {
        return UserOnline.isValid( session().get("token") ) ?
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
                return badRequest();
        }
    }

    private Result authorize() {
        JsonNode req = request().body().asJson();
        String login = req.get("login").asText();
        String password = req.get("password").asText();
        String token;
        ObjectNode result;

        if(!UserDB.isUser(login)) {
            result = Json.newObject();
            result.put("error", "Не найден пользователя с таким логином!");
            return badRequest(result);
        }
        token = UserOnline.signIn(login, password);
        if(token == null) {
            result = Json.newObject();
            result.put("error", "Неверный пароль!");
            return badRequest(result);
        }

        session().clear();
        session().put("token", token);

        return ok();
    }

    private Result unauthorize() {
        UserOnline.signOut(
                session().get("token")
        );
        session().clear();

        return ok( login.render() );
    }
}
