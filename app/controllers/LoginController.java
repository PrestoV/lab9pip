package controllers;

import play.mvc.Controller;
import play.mvc.Result;
import views.html.login;

public class LoginController extends Controller {
    public Result index() {
        return ok(login.render());
    }
}
