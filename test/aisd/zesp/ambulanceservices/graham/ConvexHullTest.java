package aisd.zesp.ambulanceservices.graham;

import aisd.zesp.ambulanceservices.geometry.Point;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ConvexHullTest {
    private Point first;
    private Point second;
    private Point third;
    private Point fourth;
    private Point fifth;
    private Point sixth;
    private Point seventh;
    private Point eighth;
    private Point ninth;

    private List<Point> points;
    private List<Point> sortedPoints;
    private List<Point> convexHullpoints;
    private List<Point> expectedSortedPoints;
    private List<Point> actualsortedPoints;

    private HashMap actualpointsMap;
    private HashMap expectedpointsMap;
    private ConvexHull testConvexHull;

    @BeforeEach
    void setUp() {


        first = new Point(6, 13);
        second = new Point(-1, 3);
        third = new Point(9, 14);
        fourth = new Point(-4, 11);
        fifth = new Point(7, 3);
        sixth = new Point(2, 15);
        seventh = new Point(5, 25);
        eighth = new Point(-3, 20);

        sortedPoints = new ArrayList<Point>();
        expectedSortedPoints = new ArrayList<Point>();
        actualsortedPoints = new ArrayList<Point>();
        points = new ArrayList<Point>();
        convexHullpoints = new ArrayList<Point>();
        testConvexHull = new ConvexHull();
        expectedpointsMap = new HashMap<Double, Point>();
        actualpointsMap = new HashMap<Double, Point>();

        points.add(first);
        points.add(second);
        points.add(third);
        points.add(fourth);
        points.add(fifth);
        points.add(sixth);
        points.add(seventh);
        points.add(eighth);

        sortedPoints.add(fourth);
        sortedPoints.add(second);
        sortedPoints.add(fifth);
        sortedPoints.add(first);
        sortedPoints.add(third);
        sortedPoints.add(sixth);
        sortedPoints.add(seventh);
        sortedPoints.add(eighth);

        convexHullpoints.add(first);
        convexHullpoints.add(second);
        convexHullpoints.add(third);

        expectedpointsMap.put(0.2, first);
        expectedpointsMap.put((-8.0 / 3.0), second);
        expectedpointsMap.put((3.0 / 13.0), third);
        expectedpointsMap.put((-8.0 / 11.0), fifth);

        expectedSortedPoints.add(fourth);
        expectedSortedPoints.add(second);
        expectedSortedPoints.add(fifth);


    }


    @Test
    void shouldChooseStartPoint() {

        Point actual = testConvexHull.chooseStartPoint(points);
        assertEquals(fourth, actual);
    }

    @Test
    void shouldChooseStartPointWhenXAreTheSame() {

        ninth = new Point(-4, 2);
        points.add(ninth);
        Point actual = testConvexHull.chooseStartPoint(points);
        assertEquals(ninth, actual);

    }

    @Test
    void shouldCalculateAngles() {

        expectedpointsMap.put((2.0 / 3.0), sixth);
        expectedpointsMap.put((14.0 / 9.0), seventh);
        expectedpointsMap.put((9.0 / 1.0), eighth);

        actualpointsMap = testConvexHull.calculateAngles(fourth, points);

        assertEquals(expectedpointsMap, actualpointsMap);

    }

    @Test
    void shouldSortByAngles() {

        expectedSortedPoints.add(first);
        expectedSortedPoints.add(third);


        sortedPoints = testConvexHull.sortByAngles(fourth, expectedpointsMap);

        assertEquals(expectedSortedPoints, sortedPoints);
    }

    @Test
    void shouldchoosePointToConvexHull() {

        expectedSortedPoints.add(third);
        expectedSortedPoints.add(seventh);
        expectedSortedPoints.add(eighth);

        actualsortedPoints = testConvexHull.choosePointToConvexHull(sortedPoints);

        assertEquals(expectedSortedPoints, actualsortedPoints);
    }

    @Test
    void shouldReturnTrueIfPointIsInConvexHull() {

        assertTrue(testConvexHull.isPointInHull(convexHullpoints, first));
    }

    @Test
    void shouldReturnFalseIfPointIsInConvexHull() {

        assertFalse(testConvexHull.isPointInHull(convexHullpoints, fifth));
    }

    @Test
    void shouldCreateConvexHull() {

        actualsortedPoints = testConvexHull.createConvexHull(points);

        expectedSortedPoints.add(third);
        expectedSortedPoints.add(seventh);
        expectedSortedPoints.add(eighth);

        assertEquals(expectedSortedPoints, actualsortedPoints);

        }
}


