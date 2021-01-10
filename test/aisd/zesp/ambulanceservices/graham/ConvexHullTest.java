package aisd.zesp.ambulanceservices.graham;

import aisd.zesp.ambulanceservices.geometry.Point;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ConvexHullTest {

    private Point pointInCovexHull;
    private Point pointOutCovexHull;

    private Point first;
    private Point second;
    private Point third;

    private List<Point> points;
    private ConvexHull testConvexHull;

    @BeforeEach
    void setUp() {

        pointInCovexHull = new Point(0, 0);
        pointOutCovexHull = new Point(6, 12);


        first = new Point(-1, -1);
        second = new Point(8, -1);
        third = new Point(5, 10);


        points = new ArrayList<Point>();

        points.add(first);
        points.add(second);
        points.add(third);

        testConvexHull = new ConvexHull(points);

    }

    @Test
    void shouldReturnTrueIfPointIsInConvexHull() {
        assertTrue(testConvexHull.isPointInHull(pointInCovexHull));
    }

    @Test
    void shouldReturnFalseIfPointIsNotInConvexHull() {
        assertFalse(testConvexHull.isPointInHull(pointOutCovexHull));
    }


}

