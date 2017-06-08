package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import models.UserDB;
import models.UserOnline;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.login;


public class LoginController extends Controller {
    public Result index() {
        if(UserOnline.isValid( session().get("token") )) {
            return redirect(routes.MainController.index());
        }
        return ok(login.render());
    }

    public Result postLogin() {
        JsonNode req = request().body().asJson();
        String login = req.get("login").asText();
        String password = req.get("password").asText();
        String token;

        if(!UserDB.isUser(login)) {
            return badRequest("Не найдено пользователя с таким логином!");
        }
        token = UserOnline.signIn(login, password);
        if(token == null) {
            return badRequest("Неверный пароль!");
        }

        session().clear();
        session().put("token", token);

        return ok();
    }
}
