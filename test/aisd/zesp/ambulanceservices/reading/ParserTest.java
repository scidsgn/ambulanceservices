package aisd.zesp.ambulanceservices.reading;

import aisd.zesp.ambulanceservices.main.State;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ParserTest {
    private State state;
    private Parser parser;

    @BeforeEach
    void setUpState() {
        state = new State();
        parser = new Parser();
    }

    @Test
    void parseFunctionsThrowOnLargeNumbers() {
        String[] buffer = new String[]{"1234567890", "1234567890", "1234567890", "1234567890", "1234567890", "123"};

        assertThrows(IllegalArgumentException.class, () -> parser.parseHospital(state, buffer));
        assertThrows(IllegalArgumentException.class, () -> parser.parseLandmark(state, buffer));
        assertThrows(IllegalArgumentException.class, () -> parser.parsePatient(state, buffer));
        assertThrows(IllegalArgumentException.class, () -> parser.parseConnection(state, buffer));
    }

    @Test
    void parseFunctionsThrowOnNullState() {
        String[] buffer = new String[]{"435", "ABCD", "QWERTY", "ZXCV", "ABC"};

        assertThrows(NullPointerException.class, () -> parser.parseHospital(null, buffer));
        assertThrows(NullPointerException.class, () -> parser.parseLandmark(null, buffer));
        assertThrows(NullPointerException.class, () -> parser.parsePatient(null, buffer));
        assertThrows(NullPointerException.class, () -> parser.parseConnection(null, buffer));
    }
}