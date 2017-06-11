package models;


import java.util.ArrayList;
import java.util.List;

abstract public class ParamArea {
    private static List<Point> points = new ArrayList<>();

    public static void addPoint(Point point) {
        point.setInArea(
                checkPoint(point.getX(), point.getY(), point.getR())
        );
        points.add(point);
    }

    public static List<Point> getPoints(Double r) {
        List<Point> scaledPoints = new ArrayList<>();
        Point scaledPoint;
        for(Point point : points) {
            scaledPoint = point.clone();
            if(r != null) {
                scaledPoint.setInArea(
                        checkPoint(point.getX(), point.getY(), r)
                );
            }
            scaledPoints.add(scaledPoint);
        }
        return scaledPoints;
    }

    private static boolean checkPoint(double x, double y, double r) {
        if(x > 0) {
            if(y > 0)
                return checkFirstRegion(x, y, r);
            else if(y < 0)
                return checkFourthRegion(x, y, r);
            else
                return checkFirstRegion(x, y, r) || checkFourthRegion(x, y, r);
        }
        else if (x < 0) {
            if(y > 0)
                return checkSecondRegion(x, y, r);
            else if(y < 0)
                return checkThirdRegion(x, y, r);
            else
                return checkSecondRegion(x, y, r) || checkThirdRegion(x, y, r);
        }
        else {
            if(y > 0)
                return checkFirstRegion(x, y, r) || checkSecondRegion(x, y, r);
            else if(y < 0)
                return checkThirdRegion(x, y, r) || checkFourthRegion(x, y, r);
            else
                return checkFirstRegion(x, y, r) || checkSecondRegion(x, y, r)
                        || checkThirdRegion(x, y, r) || checkFourthRegion(x, y, r);
        }
    }

    private static boolean checkFirstRegion(double x, double y, double r) {
        return false;
    }

    private static boolean checkSecondRegion(double x, double y, double r) {
        return x <= 0 && y >= 0
                && x >= (-r/2) && y <= r;
    }

    private static boolean checkThirdRegion(double x, double y, double r) {
        return x <= 0 && y <= 0
                && (x*x + y*y <= r*r/4);
    }

    private static boolean checkFourthRegion(double x, double y, double r) {
        return x >= 0 && y <= 0
                && x <= r && y >= (x - r)/2;
    }
}
