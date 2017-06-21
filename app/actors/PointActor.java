package actors;

import akka.actor.*;
import models.area.Point;
import models.area.PointRepository;
import models.area.UserPoint;

import java.util.ArrayList;
import java.util.List;

import static models.area.ParamArea.checkPoint;


public class PointActor extends UntypedActor {
    private final PointRepository pointRepository;

    public PointActor(PointRepository pointRepository) {
        this.pointRepository = pointRepository;
    }

    public static Props props(PointRepository pointRepository) {
        return Props.create(PointActor.class, pointRepository);
    }

    public void onReceive(Object msg) throws Exception {
        if(msg instanceof PointActorProtocol.AddPoint) {
            receiveAddPoint((PointActorProtocol.AddPoint) msg);
        } else if(msg instanceof PointActorProtocol.GetPoints) {
            receiveGetPoints((PointActorProtocol.GetPoints) msg);
        } else {
            unhandled(msg);
        }
    }

    private void receiveAddPoint(PointActorProtocol.AddPoint msg) {
        UserPoint point = msg.userPoint;
        point.setInarea(
                checkPoint(point.getX(), point.getY(), point.getR())
        );
        pointRepository.add(point);
    }

    private void receiveGetPoints(PointActorProtocol.GetPoints msg) {
        String owner = msg.owner;
        Double r = msg.r;

        List<Point> points = pointRepository.list(owner);
        List<Point> scaledPoints = new ArrayList<>();
        Point scaledPoint;
        for(Point point : points) {
            if(r != null) {
                scaledPoint = new Point(
                        point.getX() * point.getR() / r,
                        point.getY() * point.getR() / r,
                        point.getR()
                );
                scaledPoint.setInarea(
                        r >= 0 ? checkPoint(point.getX(), point.getY(), r)
                                : checkPoint(-point.getX(), -point.getY(), -r)
                );
            } else {
                scaledPoint = point.clone();
            }
            scaledPoints.add(scaledPoint);
        }

        sender().tell(scaledPoints, self());
    }
}
