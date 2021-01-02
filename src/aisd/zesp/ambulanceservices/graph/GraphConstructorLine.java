package aisd.zesp.ambulanceservices.graph;

import aisd.zesp.ambulanceservices.geometry.Point;

public class GraphConstructorLine {
    private Point start, end;
    private double length;

    public GraphConstructorLine(Point start, Point end, double length) {
        this.start = start;
        this.end = end;
        this.length = length;
    }

    public Point getEnd() {
        return end;
    }

    public double getLength() {
        return length;
    }

    public Point getStart() {
        return start;
    }
}
