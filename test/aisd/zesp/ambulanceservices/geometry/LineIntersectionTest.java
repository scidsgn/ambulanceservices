package aisd.zesp.ambulanceservices.geometry;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LineIntersectionTest {
    private Point startFirst;
    private Point endFirst;
    private Point startSecond;
    private Point endSecond;

    private LineIntersection testIntersection;

    @BeforeEach
    void setUp(){
        startFirst = new Point(0,0);
        endFirst = new Point(10,10);
        startSecond = new Point(0,10);
        endSecond = new Point(7,3);

        testIntersection = new LineIntersection();
    }

    @Test
    void intersectThrowsOnNullPoints() {
        assertThrows(IllegalArgumentException.class,
                () -> testIntersection.intersect(null, null, endSecond, endFirst)
        );
    }

    @Test
    void getTSucceeds() {
        double expected = 0.5;

        assertEquals(expected, testIntersection.getT(startFirst, endFirst, startSecond, endSecond));
    }

    @Test
    void getUSucceeds() {
        double expected = 0.7142857142857143;

        assertEquals(expected, testIntersection.getU(startFirst, endFirst, startSecond, endSecond));
    }

    @Test
    void intersectSucceeds(){
        Double[] expected = {0.5, 0.7142857142857143};

        assertArrayEquals(expected, testIntersection.intersect(startFirst, endFirst, startSecond, endSecond));
    }

    @Test
    void intersectSucceedsOnParallelLines(){
        Point x = new Point(0,0);
        Point y = new Point(0,5);
        Point a = new Point(1,0);
        Point b = new Point(1,5);

        assertNull(testIntersection.intersect(x, y, a, b));
        assertNull(testIntersection.intersect(x, a, y, b));
    }
}