package aisd.zesp.ambulanceservices.graph;

import aisd.zesp.ambulanceservices.geometry.LineIntersection;
import aisd.zesp.ambulanceservices.geometry.Point;

import java.util.*;

public class GraphConstructorAlgorithm {
    private final List<GraphConstructorLine> lines = new ArrayList<>();
    private final Set<Point> points = new HashSet<>();

    private final LineIntersection intersect = new LineIntersection();

    private void constructCutter(GraphConstructorLine cutter, List<Point> cutPoints, List<Double> positions) {
        double[] sortedPositions = positions.stream().mapToDouble(x -> x).toArray();
        Arrays.sort(sortedPositions);

        double startPosition = 0.0;
        Point startPoint = cutter.getStart();

        for (int i = 0; i < cutPoints.size(); i++) {
            double position = sortedPositions[i];
            Point point = cutPoints.get(positions.indexOf(position));

            lines.add(new GraphConstructorLine(
                    startPoint, point, cutter.getLength() * (position - startPosition)
            ));

            startPosition = position;
            startPoint = point;
        }

        points.add(cutter.getStart());
        points.add(cutter.getEnd());
        lines.add(new GraphConstructorLine(
                startPoint, cutter.getEnd(), cutter.getLength() * (1 - startPosition)
        ));
    }

    private void cutLines(GraphConstructorLine cutter, List<GraphConstructorCut> cuts) {
        List<Double> cutterPositions = new ArrayList<>();
        List<Point> cutterPoints = new ArrayList<>();

        for (GraphConstructorCut cut : cuts) {
            GraphConstructorLine line = cut.getLine();

            double cutPosition = cut.getLinePosition();
            double cutterPosition = cut.getCutterPosition();

            if (cutPosition == 0.0) {
                cutterPoints.add(line.getStart());
                cutterPositions.add(cutterPosition);
            } else if (cutPosition == 1.0) {
                cutterPoints.add(line.getEnd());
                cutterPositions.add(cutterPosition);
            } else {
                Point intersection;

                if (!cutterPositions.contains(cutterPosition)) {
                    intersection = new Point(cutPosition, line.getStart(), line.getEnd());
                    points.add(intersection);
                    cutterPoints.add(intersection);

                    cutterPositions.add(cutterPosition);
                } else {
                    intersection = cutterPoints.get(cutterPositions.indexOf(cutterPosition));
                }

                lines.remove(cut.getLine());
                lines.add(new GraphConstructorLine(
                        line.getStart(), intersection, line.getLength() * cutPosition
                ));
                lines.add(new GraphConstructorLine(
                        intersection, line.getEnd(), line.getLength() * (1 - cutPosition)
                ));
            }
        }

        constructCutter(cutter, cutterPoints, cutterPositions);
    }

    private List<GraphConstructorCut> findIntersections(GraphConstructorLine cutter) {
        ArrayList<GraphConstructorCut> cuts = new ArrayList<>();

        for (GraphConstructorLine line : lines) {
            Double[] bezierRatios = intersect.intersect(
                    line.getStart(), line.getEnd(), cutter.getStart(), cutter.getEnd()
            );

            if (
                    bezierRatios != null &&
                    bezierRatios[0] >= 0 && bezierRatios[0] <= 1 &&
                    bezierRatios[1] >= 0 && bezierRatios[1] <= 1
            ) {
                GraphConstructorCut cut = new GraphConstructorCut(line, bezierRatios[0], bezierRatios[1]);
                cuts.add(cut);
            }
        }

        return cuts;
    }

    public void addLine(Point start, Point end, double length) throws IllegalArgumentException, NullPointerException {
        if (start == null || end == null) {
            throw new NullPointerException("Wierzchołki nie mogą być puste(null).");
        }
        if (length <= 0) {
            throw new IllegalArgumentException("Długość linii musi być większa od zera.");
        }

        GraphConstructorLine cutter = new GraphConstructorLine(start, end, length);
        List<GraphConstructorCut> cuts = findIntersections(cutter);

        if (cuts.size() == 0) {
            lines.add(cutter);
            points.add(cutter.getStart());
            points.add(cutter.getEnd());
        } else {
            cutLines(cutter, cuts);
        }
    }

    public Graph<Point> constructGraph() throws NullPointerException {
        if (points.size() == 0) {
            throw new NullPointerException("Żadna linia nie została dodana do konstruktora grafa.");
        }

        Graph<Point> graph = new Graph<>();

        for (Point point : points) {
            graph.addNode(point);
        }
        graph.finalizeNodes();

        for (GraphConstructorLine line : lines) {
            graph.connectNodes(line.getStart(), line.getEnd(), line.getLength());
        }

        return graph;
    }

    public List<GraphConstructorLine> getLines() {
        return lines;
    }
}
