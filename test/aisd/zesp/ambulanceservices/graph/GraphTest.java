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

    void addDefaultMarks() {
        graph.setMark(1, true);
        graph.setMark(2, true);
        graph.setMark(3, false);
        graph.setMark(4, false);
        graph.setMark(5, true);
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

    @Test
    void finalizeNodesSucceeds() {
        addDefaultNodes();

        assertDoesNotThrow(() -> graph.finalizeNodes());
    }

    @Test
    void finalizeNodesThrowsOnEmptyGraph() {
        assertThrows(IllegalArgumentException.class, () -> graph.finalizeNodes());
    }

    @Test
    void getLengthThrowsBeforeFinalizing() {
        addDefaultNodes();

        assertThrows(NullPointerException.class, () -> graph.getLength(1, 2));
    }

    @Test
    void getLengthThrowsOnInvalidNodes() {
        addDefaultNodes();
        addDefaultConnections();

        assertThrows(IllegalArgumentException.class, () -> graph.getLength(1, 6));
    }

    @Test
    void getMarkSucceeds() {
        addDefaultNodes();
        graph.finalizeNodes();
        addDefaultMarks();

        boolean[] marks = {true, true, false, false, true};

        for (int i = 1; i <= 5; i++) {
            assertEquals(marks[i - 1], graph.getMark(i));
        }
    }

    @Test
    void getMarkThrowsBeforeFinalizing() {
        addDefaultNodes();

        assertThrows(NullPointerException.class, () -> graph.getMark(2));
    }

    @Test
    void getMarkThrowsOnInvalidNodes() {
        addDefaultNodes();
        graph.finalizeNodes();

        assertThrows(IllegalArgumentException.class, () -> graph.getMark(6));
    }

    @Test
    void getNeighborsSucceeds() {
        addDefaultNodes();
        addDefaultConnections();

        Integer[][] neighbors1 = {
                {2, 3}, {1, 3, 4}, {1, 2, 4, 5}, {2, 3}, {3}
        };

        for (int i = 1; i <= 5; i++) {
            assertArrayEquals(neighbors1[i - 1], graph.getNeighbors(i).toArray());
        }
    }

    @Test
    void getNeighborsThrowsBeforeFinalizing() {
        addDefaultNodes();

        assertThrows(NullPointerException.class, () -> graph.getNeighbors(1));
    }

    @Test
    void getNeighborsThrowsOnInvalidNode() {
        addDefaultNodes();
        addDefaultConnections();

        assertThrows(IllegalArgumentException.class, () -> graph.getNeighbors(6));
    }

    @Test
    void getNodesSucceeds() {
        addDefaultNodes();
        graph.finalizeNodes();
        addDefaultMarks();

        Integer[] allNodes = {1, 2, 3, 4, 5};
        Integer[] trueNodes = {1, 2, 5};
        Integer[] falseNodes = {3, 4};

        assertArrayEquals(allNodes, graph.getNodes().toArray());
        assertArrayEquals(trueNodes, graph.getNodes(true).toArray());
        assertArrayEquals(falseNodes, graph.getNodes(false).toArray());
    }

    @Test
    void getNodesWithMarkThrowsBeforeFinalizing() {
        addDefaultNodes();

        assertThrows(NullPointerException.class, () -> graph.getNodes(true));
        assertThrows(NullPointerException.class, () -> graph.getNodes(false));
    }

    @Test
    void getPathLengthSucceeds() {
        addDefaultNodes();
        addDefaultConnections();

        Integer[][] paths = {
                {1, 2}, {2, 3}, {4, 3},
                {1, 3, 4}, {5, 3, 4, 2, 1}
        };
        int[] lengths = {
                3, 5, 7,
                17, 30
        };

        for (int i = 0; i < paths.length; i++) {
            assertEquals(lengths[i], graph.getPathLength(paths[i]));
        }
    }

    @Test
    void getPathThrowsBeforeFinalizing() {
        addDefaultNodes();

        Integer[] path = {1, 2, 3};

        assertThrows(NullPointerException.class, () -> graph.getPathLength(path));
    }

    @Test
    void getPathThrowsOnInvalidPathNodeCount() {
        addDefaultNodes();
        addDefaultConnections();

        Integer[] path0 = {};
        Integer[] path1 = {1};

        assertThrows(IllegalArgumentException.class, () -> graph.getPathLength(path0));
        assertThrows(IllegalArgumentException.class, () -> graph.getPathLength(path1));
    }

    @Test
    void getPathThrowsOnInvalidNodes() {
        addDefaultNodes();
        addDefaultConnections();

        Integer[] path = {1, 2, 3, 6};

        assertThrows(IllegalArgumentException.class, () -> graph.getPathLength(path));
    }

    @Test
    void getPathThrowsOnInvalidConnections() {
        addDefaultNodes();
        addDefaultConnections();

        Integer[] path = {1, 5};

        assertThrows(NullPointerException.class, () -> graph.getPathLength(path));
    }

    @Test
    void setAllMarksSucceeds() {
        addDefaultNodes();
        graph.finalizeNodes();

        graph.setAllMarks(true);

        for (int i = 1; i <= 5; i++) {
            assertTrue(graph.getMark(i));
        }
    }

    @Test
    void setAllMarksThrowsBeforeFinalizing() {
        addDefaultNodes();

        assertThrows(NullPointerException.class, () -> graph.setAllMarks(true));
    }

    @Test
    void setMarkThrowsBeforeFinalizing() {
        addDefaultNodes();

        assertThrows(NullPointerException.class, () -> graph.setMark(1, true));
    }

    @Test
    void setMarkThrowsOnInvalidNodes() {
        addDefaultNodes();
        graph.finalizeNodes();

        assertThrows(IllegalArgumentException.class, () -> graph.setMark(6, true));
    }
}