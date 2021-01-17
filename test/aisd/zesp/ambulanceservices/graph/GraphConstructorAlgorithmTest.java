package aisd.zesp.ambulanceservices.graph;

import aisd.zesp.ambulanceservices.geometry.Point;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GraphConstructorAlgorithmTest {
    private GraphConstructorAlgorithm gconst;

    @BeforeEach
    void createGraphConstructor() {
        gconst = new GraphConstructorAlgorithm();
    }

    boolean compareLines(List<GraphConstructorLine> expected, List<GraphConstructorLine> actual) {
        for (GraphConstructorLine line : actual) {
            int index = expected.indexOf(line);

            if (index != -1) {
                expected.remove(index);
            }
        }

        return expected.size() == 0;
    }

    @Test
    void addLineSucceedsOnParallelSegments() {
        List<GraphConstructorLine> expected = new ArrayList<>();

        expected.add(new GraphConstructorLine(new Point(0, 0), new Point(0, 2), 2));
        expected.add(new GraphConstructorLine(new Point(2, -1), new Point(2, 3), 4));

        gconst.addLine(new Point(0, 0), new Point(0, 2), 2);
        gconst.addLine(new Point(2, -1), new Point(2, 3), 4);

        assertTrue(compareLines(expected, gconst.getLines()));
    }

    @Test
    void addLineSucceedsOnSingleIntersectionSegments() {
        List<GraphConstructorLine> expected = new ArrayList<>();

        expected.add(new GraphConstructorLine(new Point(0, 0), new Point(0, 1), 1));
        expected.add(new GraphConstructorLine(new Point(0, 1), new Point(0, 2), 1));
        expected.add(new GraphConstructorLine(new Point(-1, 1), new Point(0, 1), 2));
        expected.add(new GraphConstructorLine(new Point(0, 1), new Point(2, 1), 4));

        gconst.addLine(new Point(0, 0), new Point(0, 2), 2);
        gconst.addLine(new Point(-1, 1), new Point(2, 1), 6);

        assertTrue(compareLines(expected, gconst.getLines()));
    }

    @Test
    void addLineSucceedsOnMultipleIntersectionSegments() {
        List<GraphConstructorLine> expected = new ArrayList<>();

        expected.add(new GraphConstructorLine(new Point(0, 0), new Point(0, 1), 1));
        expected.add(new GraphConstructorLine(new Point(0, 1), new Point(0, 2), 1));
        expected.add(new GraphConstructorLine(new Point(-1, 1), new Point(0, 1), 2));
        expected.add(new GraphConstructorLine(new Point(0, 1), new Point(1, 1), 2));
        expected.add(new GraphConstructorLine(new Point(1, 1), new Point(2, 1), 2));
        expected.add(new GraphConstructorLine(new Point(0, 0), new Point(1, 1), 2));
        expected.add(new GraphConstructorLine(new Point(1, 1), new Point(2, 2), 2));

        gconst.addLine(new Point(0, 0), new Point(0, 2), 2);
        gconst.addLine(new Point(-1, 1), new Point(2, 1), 6);
        gconst.addLine(new Point(0, 0), new Point(2, 2), 4);

        assertTrue(compareLines(expected, gconst.getLines()));
    }

    @Test
    void addLineThrowsOnNullPoint() {
        assertThrows(
                NullPointerException.class,
                () -> gconst.addLine(null, new Point(0, 2), 2)
        );
        assertThrows(
                NullPointerException.class,
                () -> gconst.addLine(new Point(0, 2), null, 2)
        );
    }

    @Test
    void addLineThrowsOnInvalidLength() {
        assertThrows(
                IllegalArgumentException.class,
                () -> gconst.addLine(new Point(0, 1), new Point(0, 2), 0)
        );
        assertThrows(
                IllegalArgumentException.class,
                () -> gconst.addLine(new Point(0, 1), new Point(0, 2), -3.64)
        );
    }

    @Test
    void constructGraphSucceeds() {
        Point p1 = new Point(0, 0);
        Point p2 = new Point(0, 1);
        Point p3 = new Point(1, 1);

        gconst.addLine(p1, p2, 10);
        gconst.addLine(p2, p3, 3);
        gconst.addLine(p1, p3, 20);

        Graph<Point> graph = gconst.constructGraph();

        assertEquals(10, graph.getLength(p1, p2));
        assertEquals(3, graph.getLength(p2, p3));
        assertEquals(20, graph.getLength(p3, p1));
    }
}