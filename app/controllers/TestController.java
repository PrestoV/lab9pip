package controllers;

import play.mvc.Controller;
import play.mvc.Result;
import views.html.main;
import views.html.test;

public class TestController extends Controller {

    public Result index() {
        return ok(test.render("title"));
    }

}