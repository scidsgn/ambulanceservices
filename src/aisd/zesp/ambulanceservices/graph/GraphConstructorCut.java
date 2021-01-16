package aisd.zesp.ambulanceservices.graph;

public class GraphConstructorCut {
    private final GraphConstructorLine line;
    private final double cutterPosition, linePosition;

    public GraphConstructorCut(GraphConstructorLine line, double linePosition, double cutterPosition) {
        this.line = line;
        this.linePosition = linePosition;
        this.cutterPosition = cutterPosition;
    }

    public double getCutterPosition() {
        return cutterPosition;
    }

    public GraphConstructorLine getLine() {
        return line;
    }

    public double getLinePosition() {
        return linePosition;
    }
}
