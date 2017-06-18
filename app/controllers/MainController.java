package controllers;

import models.area.ParamArea;
import models.area.UserPoint;
import models.users.UserOnline;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import views.html.main;

import javax.inject.Inject;


public class MainController extends Controller {
    private final ParamArea paramArea;
    private final UserOnline userOnline;

    @Inject
    public MainController(ParamArea paramArea, UserOnline userOnline) {
        this.paramArea = paramArea;
        this.userOnline = userOnline;
    }

    @Security.Authenticated(Secured.class)
    public Result index() {
        return ok(main.render(
                userOnline.getLogin( session().get("token") )
        ));
    }

    @Security.Authenticated(Secured.class)
    public Result addPoint(Double x, Double y, Double r) {
        if(isValidX(x) && isValidY(y) && isValidR(r)) {
            paramArea.addPoint(
                    new UserPoint(userOnline.getLogin( session().get("token") ), x, y, r)
            );
            return getPoints(r);
        } else {
            return badRequest(
                    Json.newObject()
                            .put("error", "Неверные параметры!")
            );
        }
    }

    @Security.Authenticated(Secured.class)
    public Result getPoints(Double r) {
        return ok(
                Json.toJson( paramArea.getPoints(userOnline.getLogin( session().get("token") ), r) )
        );
    }

    private boolean isValidX(Double x) {
        return x >= -2 && x <= 2;
    }

    private boolean isValidY(Double y) {
        return y >= -3 && y <= 3;
    }

    private boolean isValidR(Double r) {
        return r >= -2 && r <= 2;
    }
}
