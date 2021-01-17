package aisd.zesp.ambulanceservices.geometry;

public class LineIntersection {

    public Double[] intersect(Point startFirst, Point endFirst, Point startSecond, Point endSecond) {
        if (startFirst == null || endFirst == null || startSecond == null || endSecond == null) {
            throw new IllegalArgumentException("Niemożliwe jest znalezienie przecięcia liń stworzonych z nieistniejących punktów.");
        }
        Double[] ratios = new Double[2];

        double denominator = (startFirst.getX() - endFirst.getX()) * (startSecond.getY() - endSecond.getY())
                - (startFirst.getY() - endFirst.getY()) * (startSecond.getX() - endSecond.getX());

        if (denominator != 0) {
            ratios[0] = getT(startFirst, endFirst, startSecond, endSecond);
            ratios[1] = getU(startFirst, endFirst, startSecond, endSecond);

            return ratios;
        }
        return null;
    }

    public double getT(Point startFirst, Point endFirst, Point startSecond, Point endSecond) {
        return ((startFirst.getX() - startSecond.getX()) * (startSecond.getY() - endSecond.getY())
                - (startFirst.getY() - startSecond.getY()) * (startSecond.getX() - endSecond.getX()))
                / ((startFirst.getX() - endFirst.getX()) * (startSecond.getY() - endSecond.getY())
                - (startFirst.getY() - endFirst.getY()) * (startSecond.getX() - endSecond.getX()));
    }

    public double getU(Point startFirst, Point endFirst, Point startSecond, Point endSecond) {
        return -((startFirst.getX() - endFirst.getX()) * (startFirst.getY() - startSecond.getY())
                - (startFirst.getY() - endFirst.getY()) * (startFirst.getX() - startSecond.getX()))
                / ((startFirst.getX() - endFirst.getX()) * (startSecond.getY() - endSecond.getY())
                - (startFirst.getY() - endFirst.getY()) * (startSecond.getX() - endSecond.getX()));
    }
}


