import play.Application;
import play.GlobalSettings;
import play.libs.F;
import play.mvc.Http;

import java.util.concurrent.CompletionStage;

import static play.mvc.Results.redirect;


public class Global extends GlobalSettings {
    @Override
    public CompletionStage<play.mvc.Result> onHandlerNotFound(Http.RequestHeader request) {
        return F.Promise.pure(redirect("/"));
    }
}
