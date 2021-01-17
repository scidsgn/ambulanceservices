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
    private List<Point> expectedSortedPoints;
    private List<Point> actualSortedPoints;

    private Map<Double, Point> actualPointsMap;
    private Map<Double, Point> expectedPointsMap;

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

        sortedPoints = new ArrayList<>();
        expectedSortedPoints = new ArrayList<>();
        actualSortedPoints = new ArrayList<>();
        points = new ArrayList<>();
        points2 = new ArrayList<>();

        expectedPointsMap = new HashMap<>();
        actualPointsMap = new HashMap<>();

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

        expectedPointsMap.put(0.2, first);
        expectedPointsMap.put((-8.0 / 3.0), second);
        expectedPointsMap.put((3.0 / 13.0), third);
        expectedPointsMap.put((-8.0 / 11.0), fifth);

        expectedSortedPoints.add(fourth);
        expectedSortedPoints.add(second);
        expectedSortedPoints.add(fifth);
    }

    @Test
    void chooseStartPointSucceeds() {
        Point actual = testGrahamAlgorithm.chooseStartPoint(points);
        assertEquals(fourth, actual);
    }

    @Test
    void chooseStartPointSucceedsOnSameXCoords() {
        ninth = new Point(-4, 2);
        points.add(ninth);

        Point actual = testGrahamAlgorithm.chooseStartPoint(points);

        assertEquals(ninth, actual);
    }

    @Test
    void calculateAnglesSucceeds() {
        expectedPointsMap.put((2.0 / 3.0), sixth);
        expectedPointsMap.put((14.0 / 9.0), seventh);
        expectedPointsMap.put((9.0 / 1.0), eighth);

        actualPointsMap = testGrahamAlgorithm.calculateAngles(fourth, points);

        assertEquals(expectedPointsMap, actualPointsMap);
    }

    @Test
    void sortByAnglesSucceds() {
        expectedSortedPoints.add(first);
        expectedSortedPoints.add(third);

        sortedPoints = testGrahamAlgorithm.sortByAngles(fourth, expectedPointsMap);

        assertEquals(expectedSortedPoints, sortedPoints);
    }

    @Test
    void choosePointsForConvexHullSucceeds() {
        expectedSortedPoints.add(third);
        expectedSortedPoints.add(seventh);
        expectedSortedPoints.add(eighth);

        sortedPoints.add(fifth);
        sortedPoints.add(first);
        sortedPoints.add(third);
        sortedPoints.add(sixth);
        sortedPoints.add(seventh);
        sortedPoints.add(eighth);

        actualSortedPoints = testGrahamAlgorithm.choosePointsForConvexHull(sortedPoints);

        assertEquals(expectedSortedPoints, actualSortedPoints);
    }

    @Test
    void ChoosePointToConvexHullThrowIllegalArgumentExeption() {
        assertThrows(IllegalArgumentException.class, () -> testGrahamAlgorithm.choosePointsForConvexHull(sortedPoints));
    }

    @Test
    void createConvexHullSucceeds() {
        points2 = new ArrayList<>();
        points2.add(fourth);
        points2.add(second);
        points2.add(fifth);
        points2.add(third);
        points2.add(seventh);
        points2.add(eighth);

        ConvexHull testConvexHull = testGrahamAlgorithm.createConvexHull(points);
        List<Point> actualConvexHullPoints = testConvexHull.getHullPoints();

        assertEquals(points2.size(), actualConvexHullPoints.size());
        for (int i = 0; i < points2.size(); i++) {
            assertEquals(points2.get(i), actualConvexHullPoints.get(i));
        }
    }
}
