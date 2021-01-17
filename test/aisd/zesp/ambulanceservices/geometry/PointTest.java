package aisd.zesp.ambulanceservices.geometry;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PointTest {
    private Point start;
    private Point end;
    private Point testPoint;

    @BeforeEach
    public void startAndEndPointsSetUp(){
        start = new Point(0, 0);
        end = new Point(10, 10);
    }

    @Test
    public void twoArgumentConstructorSucceeds(){
        testPoint = new Point(21.15, 20.20);

        assertEquals(21.15, testPoint.getX());
        assertEquals(20.20, testPoint.getY());
    }

    @Test
    public void threeArgumentConstructorSucceeds(){
        testPoint = new Point(0.5, start, end);

        assertEquals(5, testPoint.getX());
        assertEquals(5, testPoint.getY());
    }

    @Test
    public void threeArgumentConstructorSucceedsThrowsOnNullPoints(){
        assertThrows(IllegalArgumentException.class, () -> testPoint = new Point(14, null, null));
    }

    @Test
    public void getRelativeDirectionThrowsOnNullPoint(){
        testPoint = new Point(1,2);

        assertThrows(IllegalArgumentException.class, () -> testPoint.getRelativeDirection(null, end));
    }

    @Test
    public void getRelativeDirectionSucceeds(){
        testPoint = new Point(1, 2);

        double actual = testPoint.getRelativeDirection(start, end);
        assertEquals(10, actual);
    }

    @Test
    public void isLeftSucceeds() {
        Point p1 = new Point(1.1, 1.45);
        Point p2 = new Point(1.1, 0.99);

        assertTrue(p1.isLeft(start, end));
        assertFalse(p2.isLeft(start, end));
    }
}