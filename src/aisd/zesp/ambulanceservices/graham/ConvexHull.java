package aisd.zesp.ambulanceservices.graham;

import aisd.zesp.ambulanceservices.geometry.Point;

import java.util.List;

public class ConvexHull {
    private final List<Point> hullPoints;

    public ConvexHull(List<Point> points) {
        hullPoints = points;
    }

    public List<Point> getHullPoints() {
        return hullPoints;
    }

    public boolean isPointInHull(Point point) {
        for (int i = 0; i < hullPoints.size(); i++) {
            Point start = hullPoints.get(i);
            Point end = hullPoints.get((i + 1) % hullPoints.size());

            if (!point.isLeft(start, end)) {
                return false;
            }
        }

        return true;
    }

    public double[] getBoundingBox() {
        double[] bbox = {
            Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY
        };

        for (Point p : hullPoints) {
            double x = p.getX();
            double y = p.getY();

            if (bbox[0] > x) {
                bbox[0] = x;
            }
            if (bbox[2] < x) {
                bbox[2] = x;
            }

            if (bbox[1] > y) {
                bbox[1] = y;
            }
            if (bbox[3] < y) {
                bbox[3] = y;
            }
        }

        return bbox;
    }
}
