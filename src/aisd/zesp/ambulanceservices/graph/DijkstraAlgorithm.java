package aisd.zesp.ambulanceservices.graph;

import java.util.*;

public class DijkstraAlgorithm<T> {
    private final Double[] distances;
    private final Graph<T> graph;
    private final List<T> previousNodes;

    public DijkstraAlgorithm(Graph<T> graph) {
        this.graph = graph;
        distances = new Double[graph.size()];
        previousNodes = new ArrayList<>();
    }

    private T getNextNode() {
        double minDist = Double.POSITIVE_INFINITY;
        T minNode = null;

        for (int i = 0; i < graph.size(); i++) {
            T node = graph.getNode(i);

            if (graph.getMark(node)) {
                continue;
            }

            double nodeDist = distances[i];
            if (nodeDist < minDist) {
                minDist = nodeDist;
                minNode = node;
            }
        }

        return minNode;
    }

    private void iterateOverNeighbors(T node) {
        List<T> neighbors = graph.getNeighbors(node);
        double baseDistance = distances[graph.indexOf(node)];

        graph.setMark(node, true);

        for (T neighbor: neighbors) {
            if (graph.getMark(neighbor)) {
                continue;
            }

            int nIndex = graph.indexOf(neighbor);
            double length = graph.getLength(node, neighbor);

            if (baseDistance + length < distances[nIndex]) {
                distances[nIndex] = baseDistance + length;
                previousNodes.set(nIndex, node);
            }
        }
    }

    public void execute(T source) throws IllegalArgumentException {
        int nIndex = graph.indexOf(source);
        if (nIndex == -1) {
            throw new IllegalArgumentException("Wierzchołek nie jest reprezentowany w grafie.");
        }

        Arrays.fill(distances, Double.POSITIVE_INFINITY);
        distances[nIndex] = 0.0;

        previousNodes.clear();
        for (int i = 0; i < graph.size(); i++) {
            previousNodes.add(null);
        }

        graph.setAllMarks(false);

        T nextNode;
        while ((nextNode = getNextNode()) != null) {
            iterateOverNeighbors(nextNode);
        }
    }

    public List<T> getPath(T target) throws IllegalArgumentException {
        int nIndex = graph.indexOf(target);
        if (nIndex == -1) {
            throw new IllegalArgumentException("Wierzchołek nie jest reprezentowany w grafie.");
        }

        if (previousNodes.get(nIndex) == null) {
            return null;
        }

        List<T> path = new ArrayList<>();

        do {
            path.add(graph.getNode(nIndex));

            nIndex = graph.indexOf(previousNodes.get(nIndex));
        } while (nIndex != -1);

        return path;
    }
}
