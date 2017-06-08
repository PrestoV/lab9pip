package controllers;

import models.UserOnline;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Security;
import views.html.defaultpages.unauthorized;


public class Secured extends Security.Authenticator {
    @Override
    public String getUsername(Http.Context context) {
        return UserOnline.isValid( context.session().get("token") ) ?
                context.session().get("token")
                : null;
    }

    @Override
    public Result onUnauthorized(Http.Context context) {
        //TODO: redirect(routes.Application.login());
        return unauthorized(unauthorized.render());
    }
}
