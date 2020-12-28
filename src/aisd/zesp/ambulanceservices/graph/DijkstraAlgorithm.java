package aisd.zesp.ambulanceservices.graph;

import java.util.ArrayList;
import java.util.List;

public class DijkstraAlgorithm<T> {
    private final Double[] distances;
    private final Graph<T> graph;
    private final List<T> previousNodes;

    public DijkstraAlgorithm(Graph<T> graph) {
        int n = graph.getNodes().size();

        this.graph = graph;
        distances = new Double[n];
        previousNodes = new ArrayList<T>(n);
    }
}
