package aisd.zesp.ambulanceservices.graph;

import aisd.zesp.ambulanceservices.geometry.Point;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class GraphConstructorTest {
    private GraphConstructor gconst;

    @BeforeEach
    void createGraphConstructor() {
        gconst = new GraphConstructor();
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
    void addsParallelSegments() {
        List<GraphConstructorLine> expected = new ArrayList<>();

        expected.add(new GraphConstructorLine(new Point(0, 0), new Point(0, 2), 2));
        expected.add(new GraphConstructorLine(new Point(2, -1), new Point(2, 3), 4));

        gconst.addLine(new Point(0, 0), new Point(0, 2), 2);
        gconst.addLine(new Point(2, -1), new Point(2, 3), 4);

        Set<Point> points = gconst.getPoints();

        assertEquals(4, points.size());
        assertTrue(compareLines(expected, gconst.getLines()));
    }

    @Test
    void addsIntersectingSegments() {
        List<GraphConstructorLine> expected = new ArrayList<>();

        expected.add(new GraphConstructorLine(new Point(0, 0), new Point(0, 1), 1));
        expected.add(new GraphConstructorLine(new Point(0, 1), new Point(0, 2), 1));
        expected.add(new GraphConstructorLine(new Point(-1, 1), new Point(0, 1), 1));
        expected.add(new GraphConstructorLine(new Point(0, 1), new Point(2, 1), 2));

        gconst.addLine(new Point(0, 0), new Point(0, 2), 2);
        gconst.addLine(new Point(-1, 1), new Point(2, 1), 3);

        Set<Point> points = gconst.getPoints();

        assertEquals(5, points.size());
        assertTrue(compareLines(expected, gconst.getLines()));
    }
}