package aisd.zesp.ambulanceservices.geometry;

public class Point {
    private final double x;
    private final double y;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Point(double x, Point start, Point end) {
        this.x = start.getX() + (end.getX() - start.getX())/x;
        y = start.getY() + (end.getY() - start.getY())/x;
    }

    public double getRelativeDirection(Point a, Point b){
        return (b.getX() - a.getX())*(y - a.getY()) - (b.getY() - a.getY())*(x - a.getX());
    }

    public boolean isLeft(Point start, Point end){
        return getRelativeDirection(start, end) > 0;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
}
