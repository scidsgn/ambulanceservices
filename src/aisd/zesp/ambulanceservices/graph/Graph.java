package aisd.zesp.ambulanceservices.graph;

import java.util.ArrayList;
import java.util.List;

public class Graph<T> {
    private Double[][] edges = null;
    private Boolean[] marks = null;
    private List<T> nodes = new ArrayList<>();

    public void addNode(T node) throws IllegalArgumentException, NullPointerException {
        if (node == null) {
            throw new NullPointerException("Graph node can't be null.");
        }
        if (nodes.contains(node)) {
            throw new IllegalArgumentException("Node already exists within the graph.");
        }

        nodes.add(node);
    }

    public void connectNodes(T n1, T n2, double length) throws IllegalArgumentException, NullPointerException {
        if (edges == null) {
            throw new NullPointerException("Nodes must be finalized first.");
        }

        int n1Index = nodes.indexOf(n1);
        int n2Index = nodes.indexOf(n2);

        if (n1Index == -1 || n2Index == -1) {
            throw new IllegalArgumentException("Node(s) must be added to the graph first.");
        }

        edges[n1Index][n2Index] = length;
        edges[n2Index][n1Index] = length;
    }
}
