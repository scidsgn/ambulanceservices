package aisd.zesp.ambulanceservices.graph;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GraphTest {
    private Graph<Integer> graph;

    @BeforeEach
    void createGraph() {
        graph = new Graph<>();
    }

    void addDefaultNodes() {
        graph.addNode(1);
        graph.addNode(2);
        graph.addNode(3);
        graph.addNode(4);
        graph.addNode(5);
    }

    @Test
    void addNodeWillSucceed() {
        addDefaultNodes();

        List<Integer> nodes = graph.getNodes();
        Integer[] expected = {1, 2, 3, 4, 5};

        assertArrayEquals(expected, nodes.toArray());
    }

    @Test
    void addNodeThrowsOnDuplicates() {
        addDefaultNodes();

        List<Integer> nodes = graph.getNodes();

        for (Integer n : nodes) {
            assertThrows(IllegalArgumentException.class, () -> graph.addNode(n));
        }
    }

    @Test
    void addNodeThrowsOnNull() {
        assertThrows(NullPointerException.class, () -> graph.addNode(null));
    }

    @Test
    void addNodeThrowsAfterFinalizing() {
        addDefaultNodes();
        graph.finalizeNodes();

        assertThrows(NullPointerException.class, () -> graph.addNode(6));
    }
}