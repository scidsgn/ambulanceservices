package aisd.zesp.ambulanceservices.graph;

import aisd.zesp.ambulanceservices.geometry.Point;

public class GraphConstructorLine {
    private final Point start, end;
    private final double length;

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

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof GraphConstructorLine)) {
            return false;
        }

        Point oStart = ((GraphConstructorLine) obj).getStart();
        Point oEnd = ((GraphConstructorLine) obj).getEnd();
        double oLength = ((GraphConstructorLine) obj).getLength();

        boolean pointsEqual = (start.equals(oStart) && end.equals(oEnd)) ||
                              (start.equals(oEnd) && end.equals(oStart));

        return pointsEqual && length == oLength;
    }
}
