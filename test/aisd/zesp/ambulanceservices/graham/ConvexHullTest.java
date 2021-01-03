package aisd.zesp.ambulanceservices.graham;

import aisd.zesp.ambulanceservices.geometry.LineIntersection;
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


    private List<Point> unsortedPoints;
    private List<Point> sortedPoints;
    private List<Point> ConvexHullpoints;
    private List<Point> expectedSortedPoints;
    private List<Point> ActualsortedPoints;

    private HashMap ExpectedpointsMap;
    private ConvexHull testConvexHull;

    @BeforeEach
    void setUp() {

        first = new Point(6,13);
        second = new Point(-1,3);
        third = new Point(9,14);
        fourth = new Point(-4,11);
        fifth = new Point(7,3);
        sixth = new Point(2, 15);
        seventh = new Point(5, 25);
        eighth = new Point(-3, 20);


        unsortedPoints = new ArrayList<Point>();
        unsortedPoints.add(first);
        unsortedPoints.add(second);
        unsortedPoints.add(third);
        unsortedPoints.add(fourth);
        unsortedPoints.add(fifth);
        unsortedPoints.add(sixth);
        unsortedPoints.add(seventh);
        unsortedPoints.add(eighth);

        testConvexHull = new ConvexHull();

        ConvexHullpoints = new ArrayList<Point>();
        ConvexHullpoints.add(first);
        ConvexHullpoints.add(second);
        ConvexHullpoints.add(third);

        ExpectedpointsMap = new HashMap<Double, Point>();
    }

    @Test
    void shouldChooseStartPoint() {


        Point actual = testConvexHull.chooseStartPoint(unsortedPoints);
        assertEquals(fourth, actual);
    }

    @Test
    void shouldCalculateAngles() {

        HashMap ActualpointsMap = new HashMap<Double, Point>();
     //   HashMap ExpectedpointsMap = new HashMap<Double, Point>();


        System.out.println(" ok0?" + ExpectedpointsMap);
        ExpectedpointsMap.put(0.2, first);
        System.out.println(" ok?" + ExpectedpointsMap);
        ExpectedpointsMap.put((-8.0 / 3.0), second);
        System.out.println(" ok2?" + ExpectedpointsMap);
        ExpectedpointsMap.put((3.0 / 13.0), third);
        System.out.println(" ok3?" + ExpectedpointsMap);
      //  ExpectedpointsMap.put((3.0 / 13.0), fourth);
        ExpectedpointsMap.put((-8.0 / 11.0), fifth);
        System.out.println(" ok4?" + ExpectedpointsMap);
        ExpectedpointsMap.put((2.0 / 3.0), sixth);
        ExpectedpointsMap.put((14.0 / 9.0), seventh);
        ExpectedpointsMap.put((9.0 / 1.0), eighth);


        System.out.println(ExpectedpointsMap);


        ActualpointsMap = testConvexHull.calculateAngles(fourth, unsortedPoints);

       // System.out.println(ActualpointsMap);
        assertEquals(ExpectedpointsMap, ActualpointsMap);

    }

    @Test
    void shouldSortByAngles() {

        HashMap ExpectedpointsMap = new HashMap<Double, Point>();


        sortedPoints = new ArrayList<Point>();

        List<Point> expectedSortedPoints = new ArrayList<Point>();

        expectedSortedPoints.add(second);
        expectedSortedPoints.add(fifth);
        expectedSortedPoints.add(first);
        expectedSortedPoints.add(third);


        ExpectedpointsMap.put(0.2, first);
        ExpectedpointsMap.put((-8.0 / 3.0), second);
        ExpectedpointsMap.put((3.0 / 13.0), third);
        ExpectedpointsMap.put((-8.0 / 11.0), fifth);


        sortedPoints = testConvexHull.sortByAngles(ExpectedpointsMap);


        assertEquals(sortedPoints, expectedSortedPoints);

    }

    @Test
    void shouldchoosePointToConvexHull() {


        sortedPoints = new ArrayList<Point>();
        expectedSortedPoints = new ArrayList<Point>();
        ActualsortedPoints = new ArrayList<Point>();


        sortedPoints.add(second);
        sortedPoints.add(fifth);
        sortedPoints.add(first);
        sortedPoints.add(third);
        sortedPoints.add(sixth);
        sortedPoints.add(seventh);
        sortedPoints.add(eighth);



        expectedSortedPoints.add(second);
        expectedSortedPoints.add(fifth);
        expectedSortedPoints.add(third);
        expectedSortedPoints.add(seventh);
        expectedSortedPoints.add(eighth);


        ActualsortedPoints = testConvexHull.choosePointToConvexHull(sortedPoints);


        assertEquals(expectedSortedPoints, ActualsortedPoints);

    }

    @Test
    void shouldReturnTrueIfPointIsInConvexHull() {

        assertTrue(testConvexHull.isPointInHull(ConvexHullpoints, first));
    }

    @Test
    void shouldReturnFalseIfPointIsInConvexHull() {

        assertFalse(testConvexHull.isPointInHull(ConvexHullpoints, fifth));
    }



}


