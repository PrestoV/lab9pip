package controllers;

import models.users.UsersOnline;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Security;
import views.html.unauth;

import javax.inject.Inject;


public class Secured extends Security.Authenticator {
    private final UsersOnline userOnline;

    @Inject
    public Secured(UsersOnline userOnline) {
        this.userOnline = userOnline;
    }

    @Override
    public String getUsername(Http.Context context) {
        return userOnline.isValid( context.session().get("token") ) ?
                context.session().get("token")
                : null;
    }

    @Override
    public Result onUnauthorized(Http.Context context) {
        return unauthorized( unauth.render() );
    }
}
