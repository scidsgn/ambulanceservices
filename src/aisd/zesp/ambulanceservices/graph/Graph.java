package aisd.zesp.ambulanceservices.graph;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
        if (marks == null) {
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

    public List<T> getNodes(boolean mark) throws NullPointerException {
        if (marks == null) {
            throw new NullPointerException("Nodes must be finalized first.");
        }

        return nodes.stream().filter(
                node -> getMark(node) == mark
        ).collect(Collectors.toList());
    }

    public double getPathLength(T[] path) {
        return getPathLength(Arrays.asList(path));
    }

    public double getPathLength(List<T> path) throws IllegalArgumentException, NullPointerException {
        if (edges == null) {
            throw new NullPointerException("Nodes must be finalized first.");
        }
        if (path == null) {
            throw new IllegalArgumentException("Path cannot be null.");
        }
        if (path.size() < 2) {
            throw new IllegalArgumentException("Path must contain at least 2 nodes.");
        }

        double pathLength = 0;

        for (int i = 1; i < path.size(); i++) {
            T n1 = path.get(i - 1);
            T n2 = path.get(i);

            int n1Index = nodes.indexOf(n1);
            int n2Index = nodes.indexOf(n2);
            if (n1Index == -1 || n2Index == -1) {
                throw new IllegalArgumentException("Node(s) are not present in the graph.");
            }

            Double length = edges[n1Index][n2Index];
            if (length == null) {
                throw new NullPointerException("No connection found between nodes.");
            }

            pathLength += length;
        }

        return pathLength;
    }

    public void setAllMarks(boolean mark) throws NullPointerException {
        if (marks == null) {
            throw new NullPointerException("Nodes must be finalized first.");
        }

        Arrays.fill(marks, mark);
    }

    public void setMark(T node, boolean mark) throws IllegalArgumentException, NullPointerException {
        if (marks == null) {
            throw new NullPointerException("Nodes must be finalized first.");
        }

        int nIndex = nodes.indexOf(node);
        if (nIndex == -1) {
            throw new IllegalArgumentException("Node is not present in the graph.");
        }

        marks[nIndex] = mark;
    }
}
