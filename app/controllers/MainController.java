package controllers;

import models.ParamArea;
import models.Point;
import models.UserOnline;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import views.html.main;


public class MainController extends Controller {
    @Security.Authenticated(Secured.class)
    public Result index() {
        return ok(main.render(
                UserOnline.getLogin( session().get("token") )
        ));
    }

    public Result addPoint(Double x, Double y, Double r) {
        if(isValidX(x) && isValidY(y) && isValidR(r)) {
            ParamArea.addPoint(
                    new Point(x, y, r)
            );
            return getPoints(r);
        } else {
            return badRequest(
                    Json.newObject()
                            .put("error", "Неверные параметры!")
            );
        }
    }

    public Result getPoints(Double r) {
        return ok(
                Json.toJson( ParamArea.getPoints(r) )
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
