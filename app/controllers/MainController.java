package controllers;

import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import views.html.main;


public class MainController extends Controller {
    @Security.Authenticated(Secured.class)
    public Result index() {
        return ok(main.render());
    }
}
