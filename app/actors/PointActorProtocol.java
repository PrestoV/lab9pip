package actors;

import models.area.UserPoint;


public class PointActorProtocol {
    public static class AddPoint {
        public final UserPoint userPoint;

        public AddPoint(UserPoint point) {
            this.userPoint = point;
        }
    }

    public static class GetPoints {
        public final String owner;
        public final Double r;

        public GetPoints(String owner, Double r) {
            this.owner = owner;
            this.r = r;
        }
    }
}
