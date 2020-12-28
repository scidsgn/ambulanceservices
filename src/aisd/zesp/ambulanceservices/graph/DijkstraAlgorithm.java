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

    public List<T> getPath(T target) {
        List<T> nodes = graph.getNodes();

        int nIndex = nodes.indexOf(target);
        if (nIndex == -1) {
            throw new IllegalArgumentException("Node is not present in the graph.");
        }

        if (previousNodes.get(nIndex) == null) {
            return null;
        }

        List<T> path = new ArrayList<>();

        do {
            path.add(nodes.get(nIndex));

            nIndex = nodes.indexOf(previousNodes.get(nIndex));
        } while (nIndex != -1);

        return path;
    }
}
