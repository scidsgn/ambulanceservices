package aisd.zesp.ambulanceservices.graham;

import aisd.zesp.ambulanceservices.geometry.Point;

import java.util.*;
import java.lang.Double;

public class GrahamAlgorithm {
    public ConvexHull createConvexHull(List<Point> points) {
        Point startPoint = chooseStartPoint(points);
        Map<Double, Point> pointsMap = calculateAngles(startPoint, points);
        List<Point> sortedPoints = sortByAngles(startPoint, pointsMap);

        return new ConvexHull(choosePointsForConvexHull(sortedPoints));
    }

    public Point chooseStartPoint(List<Point> points) {
        Collections.sort(points);

        return points.get(0);
    }

    public Map<Double, Point> calculateAngles(Point startPoint, List<Point> points) {
        Map<Double, Point> pointsMap = new HashMap<>();
        double angle;

        for (Point point : points) {
            if (point != startPoint) {
                angle = (point.getY() - startPoint.getY()) / (point.getX() - startPoint.getX());

                if (pointsMap.containsKey(angle)) {
                    if (
                            Math.abs(
                                    pointsMap.get(angle).getY() - startPoint.getY()
                            ) <= Math.abs(point.getY() - startPoint.getY())
                    ) {
                        pointsMap.put(angle, point);
                    }
                } else {
                    pointsMap.put(angle, point);
                }
            }
        }

        return pointsMap;
    }

    public List<Point> sortByAngles(Point startPoint, Map<Double, Point> pointsMap) {
        List<Point> sortedPoints = new ArrayList<>();

        List<Double> pointsByAngles = new ArrayList<>(pointsMap.keySet());
        Collections.sort(pointsByAngles);
        sortedPoints.add(startPoint);

        for (Double x : pointsByAngles) {
            sortedPoints.add(pointsMap.get(x));
        }

        return sortedPoints;
    }

    public List<Point> choosePointsForConvexHull(List<Point> sortedPoints) {
        if (sortedPoints.size() < 3){
            throw new IllegalArgumentException("Mapa musi zawierać minimum 3 punkty, spośród których minimum jeden nie należy do wspólnej prostej.");
        }

        List<Point> convexHullPoints = new ArrayList<>();

        convexHullPoints.add(sortedPoints.get(0));
        convexHullPoints.add(sortedPoints.get(1));
        convexHullPoints.add(sortedPoints.get(2));

        for (int i = 3; i < sortedPoints.size(); i++) {
            Point point = sortedPoints.get(i);
            int last = convexHullPoints.size() - 1;

            while (!point.isLeft(convexHullPoints.get(last - 1), convexHullPoints.get(last))) {
                convexHullPoints.remove(last);
                last--;
            }

            convexHullPoints.add(sortedPoints.get(i));
        }

        return convexHullPoints;
    }

}

