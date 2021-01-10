package aisd.zesp.ambulanceservices.graham;

import aisd.zesp.ambulanceservices.geometry.Point;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class GrahamAlgorithmTest {
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
    private List<Point> points2;
    private List<Point> sortedPoints;
    private List<Point> convexHullpoints;
    private List<Point> expectedSortedPoints;
    private List<Point> actualsortedPoints;

    private Map<Double, Point> actualpointsMap;
    private HashMap expectedpointsMap;

    private GrahamAlgorithm testGrahamAlgorithm;


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
        points2 = new ArrayList<Point>();
        convexHullpoints = new ArrayList<Point>();

        expectedpointsMap = new HashMap<Double, Point>();
        actualpointsMap = new HashMap<Double, Point>();

        testGrahamAlgorithm = new GrahamAlgorithm();

        points.add(fourth);
        points.add(first);
        points.add(third);
        points.add(second);
        points.add(eighth);
        points.add(seventh);
        points.add(fifth);
        points.add(sixth);

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

        Point actual = testGrahamAlgorithm.chooseStartPoint(points);
        assertEquals(fourth, actual);
    }

    @Test
    void shouldChooseStartPointWhenXAreTheSame() {

        ninth = new Point(-4, 2);
        points.add(ninth);
        Point actual = testGrahamAlgorithm.chooseStartPoint(points);
        assertEquals(ninth, actual);

    }


    @Test
    void shouldCalculateAngles() {

        expectedpointsMap.put((2.0 / 3.0), sixth);
        expectedpointsMap.put((14.0 / 9.0), seventh);
        expectedpointsMap.put((9.0 / 1.0), eighth);

        actualpointsMap = testGrahamAlgorithm.calculateAngles(fourth, points);

        assertEquals(expectedpointsMap, actualpointsMap);

    }

    @Test
    void shouldSortByAngles() {

        expectedSortedPoints.add(first);
        expectedSortedPoints.add(third);


        sortedPoints = testGrahamAlgorithm.sortByAngles(fourth, expectedpointsMap);

        assertEquals(expectedSortedPoints, sortedPoints);
    }

    @Test
    void shouldchoosePointToConvexHull() {

        expectedSortedPoints.add(third);
        expectedSortedPoints.add(seventh);
        expectedSortedPoints.add(eighth);

        actualsortedPoints = testGrahamAlgorithm.choosePointsForConvexHull(sortedPoints);

        assertEquals(expectedSortedPoints, actualsortedPoints);
    }

/*

    @Test
    void shouldCreateConvexHull() {

        points2 = new ArrayList<Point>();

        points2.add(fourth);
        points2.add(second);
        points2.add(fifth);
        points2.add(third);
        points2.add(seventh);
        points2.add(eighth);

        expectedSortedPoints2 = new ConvexHull(points2);

        testConvexHull2 = testConvexHull.createConvexHull(points);



        assertEquals(testConvexHull2, expectedSortedPoints2);

    }


 */


}
