package aisd.zesp.ambulanceservices.graph;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DijkstraAlgorithmTest {
    private Graph<Integer> graph;
    private DijkstraAlgorithm<Integer> algo;

    @BeforeEach
    void createObjects() {
        graph = new Graph<>();
        for (int i = 1; i <= 5; i++) {
            graph.addNode(i);
        }

        graph.finalizeNodes();
        graph.connectNodes(1, 2, 10);
        graph.connectNodes(1, 3, 3);
        graph.connectNodes(2, 3, 4);
        graph.connectNodes(2, 4, 5);
        graph.connectNodes(3, 4, 7);
        graph.connectNodes(5, 3, 2);
        graph.connectNodes(5, 4, 1);

        algo = new DijkstraAlgorithm<>(graph);
    }

    @Test
    void algorithmSucceeds() {
        algo.execute(1);

        List<Integer> path15 = algo.getPath(5);
        List<Integer> path14 = algo.getPath(4);

        assertEquals(5, graph.getPathLength(path15));
        assertEquals(6, graph.getPathLength(path14));

        algo.execute(2);

        List<Integer> path25 = algo.getPath(5);
        List<Integer> path21 = algo.getPath(1);

        assertEquals(6, graph.getPathLength(path25));
        assertEquals(7, graph.getPathLength(path21));

        algo.execute(3);

        List<Integer> path34 = algo.getPath(4);

        assertEquals(3, graph.getPathLength(path34));
    }

    @Test
    void executeThrowsOnInvalidNodes() {
        assertThrows(IllegalArgumentException.class, () -> algo.execute(6));
    }

    @Test
    void getPathThrowsOnInvalidNodes() {
        algo.execute(1);

        assertThrows(IllegalArgumentException.class, () -> algo.getPath(6));
    }
}