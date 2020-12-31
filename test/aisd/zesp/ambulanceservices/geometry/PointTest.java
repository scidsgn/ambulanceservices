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
        start = new Point(0,0);
        end = new Point(10,10);
    }
    @Test
    public void willThrowExceptionWithWrongArguments(){
        assertThrows(IllegalArgumentException.class, () -> testPoint = new Point(14,null, null));
    }

    @Test
    public void checkCoordinatesWithFirstConstructor(){
        testPoint = new Point(21.15,20.20);

        assertEquals(21.15,testPoint.getX());
        assertEquals(20.20,testPoint.getY());
    }

    @Test
    public void checkCoordinatesWithSecondConstructor(){
        testPoint = new Point(0.5, start, end);

        assertEquals(5,testPoint.getX());
        assertEquals(5,testPoint.getY());
    }

    @Test
    public void directionWithWrongPointsThrowsException(){
        testPoint = new Point(1,2);

        assertThrows(IllegalArgumentException.class, () -> testPoint.getRelativeDirection(null, end));
    }

    @Test
    public void directionCheck(){
        testPoint = new Point(1,2);

        double actual = testPoint.getRelativeDirection(start, end);
        assertEquals(10,actual);
    }

    @Test
    public void shouldBeLeft() {
        testPoint = new Point(1.1,1.45);

        assertTrue( testPoint.isLeft(start, end));
    }

    @Test
    public void shouldBeRight() {
        testPoint = new Point(1.1,0.99);

        assertFalse(testPoint.isLeft(start, end));
    }
}