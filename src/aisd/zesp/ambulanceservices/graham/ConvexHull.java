package aisd.zesp.ambulanceservices.graham;

import aisd.zesp.ambulanceservices.geometry.Point;

import java.util.*;
import java.lang.Double;

public class ConvexHull {

    private List<Point> points;
    private List<Point> sortedPoints;
    private List<Point> ConvexHullpoints;
    private Point startPoint;
    private HashMap<Double, Point> pointsMap;
    private Point point;

    public ConvexHull() {
        points = new ArrayList<Point>();
        sortedPoints = new ArrayList<Point>();
        ConvexHullpoints = new ArrayList<Point>();
        pointsMap = new HashMap<Double, Point>();
    }

    public List<Point> createConvexHull(List<Point> points){

        startPoint = chooseStartPoint(points);
        pointsMap = calculateAngles(startPoint, points);
        sortedPoints = sortByAngles(startPoint, pointsMap);
        ConvexHullpoints = choosePointToConvexHull (sortedPoints);

        return ConvexHullpoints;
    }


    public Point chooseStartPoint(List<Point> points) {
        Collections.sort(points);
        Point startPoint = points.get(0);
        return startPoint;
    }

    public HashMap<Double, Point> calculateAngles(Point startPoint, List<Point> points) {

        for (int i = 0; i < points.size(); i++) {
            if (points.get(i) != startPoint) {
                double angle = (points.get(i).getY() - startPoint.getY()) / (points.get(i).getX() - startPoint.getX());
                pointsMap.put(angle, points.get(i));
            }
        }
        return pointsMap;
    }

    public List<Point> sortByAngles(Point startPoint, HashMap<Double, Point> pointsMap) {

        List<Double> pointsByAngels = new ArrayList<>(pointsMap.keySet());
        Collections.sort(pointsByAngels);
        sortedPoints.add(startPoint);

        for (Double x : pointsByAngels) {
            sortedPoints.add(pointsMap.get(x));
        }
        return sortedPoints;
    }

    public List<Point> choosePointToConvexHull(List<Point> sortedPoints) {

        int size = sortedPoints.size();
        int i = 3;

        ConvexHullpoints.add(sortedPoints.get(0));
        ConvexHullpoints.add(sortedPoints.get(1));
        ConvexHullpoints.add(sortedPoints.get(2));

        for (i = 3; i < size; i++) {
            point = sortedPoints.get(i);
            int last = ConvexHullpoints.size() - 1;
            if (!point.isLeft(ConvexHullpoints.get(last - 1), ConvexHullpoints.get(last))) {
                ConvexHullpoints.remove(last);
            }
            ConvexHullpoints.add(sortedPoints.get(i));
        }
        return ConvexHullpoints;
    }

    public boolean isPointInHull(List<Point> ConvexHullpoints, Point point) {
        boolean flag = false;
        for (int i = 0; i < ConvexHullpoints.size(); i++) {
            if (ConvexHullpoints.get(i) == point) {
                flag = true;
                break;
            }
        }
        return flag;
    }

}

