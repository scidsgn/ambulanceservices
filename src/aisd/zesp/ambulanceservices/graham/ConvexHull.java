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
}
