package aisd.zesp.ambulanceservices.graph;

import java.util.*;
import java.util.stream.Collectors;

public class Graph<T> {
    private Double[][] edges = null;
    private boolean[] marks = null;
    private final List<T> nodes = new ArrayList<>();

    public void addNode(T node) throws IllegalArgumentException, NullPointerException {
        if (edges != null) {
            throw new NullPointerException("Nie można dodać więcej wierzchołków, po sfinalizowaniu grafu.");
        }

        if (node == null) {
            throw new NullPointerException("Wierzchołek grafu nie może być pusty (null).");
        }
        if (nodes.contains(node)) {
            throw new IllegalArgumentException("Ten wierzchołek znajduje się już w grafie.");
        }

        nodes.add(node);
    }

    public void connectNodes(T n1, T n2, double length) throws IllegalArgumentException, NullPointerException {
        if (edges == null) {
            throw new NullPointerException("Wierzchołki muszą być najpierw sfinalizowane.");
        }

        int n1Index = nodes.indexOf(n1);
        int n2Index = nodes.indexOf(n2);
        if (n1Index == -1 || n2Index == -1) {
            throw new IllegalArgumentException("Wierzchołek/wierzchołki nie są rezpresentowane w grafie.");
        }

        if (edges[n1Index][n2Index] != null && edges[n1Index][n2Index] != length) {
            throw new IllegalArgumentException("Wierzchołki są już połączone.");
        }

        edges[n1Index][n2Index] = length;
        edges[n2Index][n1Index] = length;
    }

    public void finalizeNodes() throws IllegalArgumentException {
        if (nodes.size() == 0) {
            throw new IllegalArgumentException("Nie można sfinalizować pustego grafu.");
        }

        int n = nodes.size();

        edges = new Double[n][n];
        marks = new boolean[n];

        Arrays.fill(marks, false);
    }

    public Double getLength(T n1, T n2) throws IllegalArgumentException, NullPointerException {
        if (edges == null) {
            throw new NullPointerException("Wierzchołki muszą być najpierw sfinalizowane.");
        }

        int n1Index = nodes.indexOf(n1);
        int n2Index = nodes.indexOf(n2);
        if (n1Index == -1 || n2Index == -1) {
            throw new IllegalArgumentException("Wierzchołek/wierzchołki nie są rezprezentowane w grafie.");
        }

        return edges[n1Index][n2Index];
    }

    public boolean getMark(T node) throws IllegalArgumentException, NullPointerException {
        if (marks == null) {
            throw new NullPointerException("Wierzchołki muszą być najpierw sfinalizowane.");
        }

        int nIndex = nodes.indexOf(node);
        if (nIndex == -1) {
            throw new IllegalArgumentException("Wierzchołek nie jest rezprezentowany w grafie.");
        }

        return marks[nIndex];
    }

    public List<T> getNeighbors(T node) throws IllegalArgumentException, NullPointerException {
        if (edges == null) {
            throw new NullPointerException("Wierzchołki muszą być najpierw sfinalizowane.");
        }

        int nIndex = nodes.indexOf(node);
        if (nIndex == -1) {
            throw new IllegalArgumentException("Wierzchołek nie jest rezprezentowany w grafie.");
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

    public T getNode(int index) {
        return nodes.get(index);
    }

    public List<T> getNodes() {
        return nodes;
    }

    public List<T> getNodes(boolean mark) throws NullPointerException {
        if (marks == null) {
            throw new NullPointerException("Wierzchołki muszą być najpierw sfinalizowane.");
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
            throw new NullPointerException("Wierzchołki muszą być najpierw sfinalizowane.");
        }
        if (path == null) {
            throw new IllegalArgumentException("Ścieżka w grafie nie może być pusta");
        }
        if (path.size() < 2) {
            throw new IllegalArgumentException("Ścieżka w grafie musi zawierać minimum 2 wierzchołki");
        }

        double pathLength = 0;

        for (int i = 1; i < path.size(); i++) {
            T n1 = path.get(i - 1);
            T n2 = path.get(i);

            int n1Index = nodes.indexOf(n1);
            int n2Index = nodes.indexOf(n2);
            if (n1Index == -1 || n2Index == -1) {
                throw new IllegalArgumentException("Wierzchołek/wierzchołki nie są rezprezentowane w grafie.");
            }

            Double length = edges[n1Index][n2Index];
            if (length == null) {
                throw new NullPointerException("Nie można znaleźć połączenia między wierzchołkami ");
            }

            pathLength += length;
        }

        return pathLength;
    }

    public int indexOf(T node) {
        return nodes.indexOf(node);
    }

    public void setAllMarks(boolean mark) throws NullPointerException {
        if (marks == null) {
            throw new NullPointerException("Wierzchołki muszą być najpierw sfinalizowane.");
        }

        Arrays.fill(marks, mark);
    }

    public void setMark(T node, boolean mark) throws IllegalArgumentException, NullPointerException {
        if (marks == null) {
            throw new NullPointerException("Wierzchołki muszą być najpierw sfinalizowane.");
        }

        int nIndex = nodes.indexOf(node);
        if (nIndex == -1) {
            throw new IllegalArgumentException("Wierzchołek nie jest rezprezentowany w grafie.");
        }

        marks[nIndex] = mark;
    }

    public int size() {
        return nodes.size();
    }
}
