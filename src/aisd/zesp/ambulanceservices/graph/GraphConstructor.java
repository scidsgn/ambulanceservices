package aisd.zesp.ambulanceservices.graph;

import aisd.zesp.ambulanceservices.geometry.LineIntersection;
import aisd.zesp.ambulanceservices.geometry.Point;

import java.util.*;

public class GraphConstructor {
    private final List<GraphConstructorLine> lines = new ArrayList<>();
    private final Set<Point> points = new HashSet<>();

    private final LineIntersection intersect = new LineIntersection();

    private void cutLines(GraphConstructorLine cutter, List<GraphConstructorCut> cuts) {}

    private List<GraphConstructorCut> findIntersections(GraphConstructorLine cutter) {
        ArrayList<GraphConstructorCut> cuts = new ArrayList<>();

        for (GraphConstructorLine line : lines) {
            Double[] bezierRatios = intersect.intersect(
                    line.getStart(), line.getEnd(), cutter.getStart(), cutter.getEnd()
            );

            if (
                    bezierRatios[0] >= 0 && bezierRatios[0] <= 1 &&
                    bezierRatios[1] >= 1 && bezierRatios[1] <= 1
            ) {
                GraphConstructorCut cut = new GraphConstructorCut(line, bezierRatios[0], bezierRatios[1]);
                cuts.add(cut);
            }
        }

        return cuts;
    }

    public void addLine(Point start, Point end, double length) {
        GraphConstructorLine cutter = new GraphConstructorLine(start, end, length);
        List<GraphConstructorCut> cuts = findIntersections(cutter);

        cutLines(cutter, cuts);
    }
}
