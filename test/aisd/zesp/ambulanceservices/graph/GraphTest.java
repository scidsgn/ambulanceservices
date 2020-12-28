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

    void addDefaultConnections() {
        graph.finalizeNodes();

        for (int i = 1; i <= 3; i++) {
            graph.connectNodes(i, i + 1, 2 * i + 1);
            graph.connectNodes(i, i + 2, 10);
        }
    }

    @Test
    void addNodeSucceeds() {
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

    @Test
    void connectNodesSucceeds() {
        addDefaultNodes();
        addDefaultConnections();

        for (int i = 1; i <= 3; i++) {
            assertEquals(2 * i + 1, graph.getLength(i, i + 1));
            assertEquals(10, graph.getLength(i, i + 2));

            assertEquals(2 * i + 1, graph.getLength(i + 1, i));
            assertEquals(10, graph.getLength(i + 2, i));
        }
    }

    @Test
    void connectNodesThrowsBeforeFinalizing() {
        addDefaultNodes();

        assertThrows(NullPointerException.class, () -> graph.connectNodes(1, 3, 6));
    }

    @Test
    void connectNodesThrowsOnInvalidNodes() {
        addDefaultNodes();
        addDefaultConnections();

        assertThrows(IllegalArgumentException.class, () -> graph.connectNodes(1, 6, 6));
    }

    @Test
    void connectNodesThrowsOnAlreadyConnected() {
        addDefaultNodes();
        addDefaultConnections();

        assertThrows(IllegalArgumentException.class, () -> graph.connectNodes(1, 3, 6));
    }
}