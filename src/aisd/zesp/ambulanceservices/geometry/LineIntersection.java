package aisd.zesp.ambulanceservices.geometry;

public class LineIntersection {

    public double intersect(Point startFirst, Point endFirst, Point startSecond, Point endSecond){
        if(startFirst == null || endFirst == null || startSecond == null || endSecond == null){
            throw new IllegalArgumentException("Intersecting lines defined by non existent points  is impossible.");
        }
        double ratio = 0;
        double denominator = (startFirst.getX() - endFirst.getX())*(startSecond.getY() - endSecond.getY())
                - (startFirst.getY() - endFirst.getY())*(startSecond.getX() - endSecond.getX());

        if(denominator != 0) {
            double t = getT(startFirst, endFirst, startSecond, endSecond);
            double u = getU(startFirst, endFirst, startSecond, endSecond);

            if(t >= 0 && t <= 1){
                ratio = t;
            }
            else if(u >= 0 && u <= 1 ){
                ratio = u;
            }
        }
        return ratio;
    }

    public double getT(Point startFirst, Point endFirst, Point startSecond, Point endSecond){
        return ((startFirst.getX() - startSecond.getX())*(startSecond.getY() - endSecond.getY())
                - (startFirst.getY() - startSecond.getY())*(startSecond.getX() - endSecond.getX()))
                / ((startFirst.getX() - endFirst.getX())*(startSecond.getY() - endSecond.getY())
                - (startFirst.getY() - endFirst.getY())*(startSecond.getX() - endSecond.getX()) );
    }

    public double getU(Point startFirst, Point endFirst, Point startSecond, Point endSecond){
        return -((startFirst.getX() - endFirst.getX())*(startFirst.getY() - startSecond.getY())
                - (startFirst.getY() - endFirst.getY())*(startFirst.getX() - startSecond.getX()))
                / ((startFirst.getX() - endFirst.getX())*(startSecond.getY() - endSecond.getY())
                - (startFirst.getY() - endFirst.getY())*(startSecond.getX() - endSecond.getX()) );
    }
}


