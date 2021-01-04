package aisd.zesp.ambulanceservices.geometry;

import java.util.Objects;

public class Point implements Comparable<Point>{
    private double x;
    private double y;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Point(double x, Point start, Point end) {
        if(start == null || end == null){
            throw new IllegalArgumentException("Neither point can be null!");
        }
        this.x = start.getX() + (end.getX() - start.getX())*x;
        y = start.getY() + (end.getY() - start.getY())*x;
    }


    public double getRelativeDirection(Point a, Point b){
        if(a == null || b == null){
            throw new IllegalArgumentException("Neither point can be null!");
        }
        return (b.getX() - a.getX())*(y - a.getY()) - (b.getY() - a.getY())*(x - a.getX());
    }

    public boolean isLeft(Point start, Point end){
        return getRelativeDirection(start, end) > 0;
    }

    public Double getX() {
        return x;
    }

    public Double getY() {
        return y;
    }

    @Override
    public int compareTo(Point o) {
        int com = this.getX().compareTo(o.getX());
        if( com != 0){
            return  com;
        }
        return this.getY().compareTo(o.getY());
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Point)) {
            return false;
        }

        return x == ((Point) obj).getX() && y == ((Point) obj).getY();
    }
}
