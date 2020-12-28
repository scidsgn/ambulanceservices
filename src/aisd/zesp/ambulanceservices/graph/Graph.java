package aisd.zesp.ambulanceservices.graph;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Graph<T> {
    private Double[][] edges = null;
    private boolean[] marks = null;
    private List<T> nodes = new ArrayList<>();

    public void addNode(T node) throws IllegalArgumentException, NullPointerException {
        if (edges != null) {
            throw new NullPointerException("Cannot add more nodes after finalizing them.");
        }

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
            throw new IllegalArgumentException("Node(s) are not present in the graph.");
        }

        if (edges[n1Index][n2Index] != null) {
            throw new IllegalArgumentException("Nodes are already connected.");
        }

        edges[n1Index][n2Index] = length;
        edges[n2Index][n1Index] = length;
    }

    public void finalizeNodes() throws IllegalArgumentException {
        if (nodes.size() == 0) {
            throw new IllegalArgumentException("Cannot finalize an empty graph.");
        }

        int n = nodes.size();

        edges = new Double[n][n];
        marks = new boolean[n];

        Arrays.fill(marks, false);
    }

    public Double getLength(T n1, T n2) throws IllegalArgumentException, NullPointerException {
        if (edges == null) {
            throw new NullPointerException("Nodes must be finalized first.");
        }

        int n1Index = nodes.indexOf(n1);
        int n2Index = nodes.indexOf(n2);
        if (n1Index == -1 || n2Index == -1) {
            throw new IllegalArgumentException("Node(s) are not present in the graph.");
        }

        return edges[n1Index][n2Index];
    }

    public boolean getMark(T node) throws IllegalArgumentException, NullPointerException {
        if (edges == null) {
            throw new NullPointerException("Nodes must be finalized first.");
        }

        int nIndex = nodes.indexOf(node);
        if (nIndex == -1) {
            throw new IllegalArgumentException("Node is not present in the graph.");
        }

        return marks[nIndex];
    }

    public List<T> getNeighbors(T node) throws IllegalArgumentException, NullPointerException {
        if (edges == null) {
            throw new NullPointerException("Nodes must be finalized first.");
        }

        int nIndex = nodes.indexOf(node);
        if (nIndex == -1) {
            throw new IllegalArgumentException("Node is not present in the graph.");
        }

        ArrayList<T> neighbors = new ArrayList<>();

        for (int i = 0; i < edges.length; i++) {
            if (i == nIndex) {
                continue;
            }
            if (edges[nIndex][i] != null) {
                neighbors.add(nodes.get(i));
            }
        }

        return neighbors;
    }

    public List<T> getNodes() {
        return nodes;
    }
}
