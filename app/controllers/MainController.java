package controllers;

import actors.PointActor;
import actors.PointActorProtocol;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import models.area.PointRepository;
import models.area.UserPoint;
import models.users.UsersOnline;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import scala.compat.java8.FutureConverters;
import views.html.main;

import javax.inject.Inject;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import static akka.pattern.Patterns.ask;


public class MainController extends Controller {
    private final UsersOnline userOnline;
    private final ActorRef pointProducer;

    @Inject
    public MainController(UsersOnline userOnline, ActorSystem actorSystem, PointRepository pointRepository) {
        this.userOnline = userOnline;
        this.pointProducer = actorSystem.actorOf( PointActor.props(pointRepository) );
    }

    @Security.Authenticated(Secured.class)
    public Result index() {
        return ok(main.render(
                userOnline.getLogin( session().get("token") )
        ));
    }

    @Security.Authenticated(Secured.class)
    public CompletionStage<Result> addPoint(Double x, Double y, Double r) {
        if(isValidX(x) && isValidY(y) && isValidR(r)) {
            pointProducer.tell(
                    new PointActorProtocol.AddPoint(
                            new UserPoint(userOnline.getLogin( session().get("token") ), x, y, r)
                    ),
                    ActorRef.noSender()
            );
            return getPoints(r);
        } else {
            return CompletableFuture.completedFuture(
                    badRequest(
                            Json.newObject().put("error", "Неверные параметры!")
                    )
            );
        }
    }

    @Security.Authenticated(Secured.class)
    public CompletionStage<Result> getPoints(Double r) {
        return FutureConverters.toJava(
                ask(pointProducer,
                        new PointActorProtocol.GetPoints(userOnline.getLogin( session().get("token") ), r),
                        1000)
        ).thenApply(
                points -> ok( Json.toJson(points) )
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
