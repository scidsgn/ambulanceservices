package aisd.zesp.ambulanceservices.geometry;

public class Point implements Comparable<Point> {
    private double x;
    private double y;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Point(double x, Point start, Point end) {
        if (start == null || end == null) {
            throw new IllegalArgumentException("Należy podać punkty początku/końca linii, nie mogą one być puste (null).");
        }
        this.x = start.getX() + (end.getX() - start.getX()) * x;
        y = start.getY() + (end.getY() - start.getY()) * x;
    }


    public double getRelativeDirection(Point a, Point b) {
        if (a == null || b == null) {
            throw new IllegalArgumentException("Należy podać punkty tworzące linie, nie mogą one być puste (null).");
        }
        return (b.getX() - a.getX()) * (y - a.getY()) - (b.getY() - a.getY()) * (x - a.getX());
    }

    public boolean isLeft(Point start, Point end) {
        return getRelativeDirection(start, end) >= 0;
    }

    public Double getX() {
        return x;
    }

    public Double getY() {
        return y;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getDistance(Point point) throws NullPointerException {
        if (point == null) {
            throw new NullPointerException("Należy podać punkt, nie może on być pusty (null).");
        }

        return Math.sqrt(Math.pow(x - point.getX(), 2) + Math.pow(y - point.getY(), 2));
    }

    @Override
    public int compareTo(Point o) {
        int com = this.getX().compareTo(o.getX());
        if (com != 0) {
            return com;
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
