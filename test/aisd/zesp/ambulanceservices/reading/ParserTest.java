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
    void hugeNumbersTest() {
        String[] buffer = new String[]{"1234567890", "1234567890", "1234567890", "1234567890", "1234567890", "2115"};

        assertThrows(IllegalArgumentException.class, () -> parser.parseHospital(state, buffer));
        assertThrows(IllegalArgumentException.class, () -> parser.parseLandmark(state, buffer));
        assertThrows(IllegalArgumentException.class, () -> parser.parsePatient(state, buffer));
        assertThrows(IllegalArgumentException.class, () -> parser.parseConnection(state, buffer));
    }

    @Test
    void willNotParseWithNullState() {
        String[] buffer = new String[]{"2115", "Masno", "BOR Crew", "SB Maffija"};

        assertThrows(IllegalArgumentException.class, () -> parser.parseHospital(null, buffer));
        assertThrows(IllegalArgumentException.class, () -> parser.parseLandmark(null, buffer));
        assertThrows(IllegalArgumentException.class, () -> parser.parsePatient(null, buffer));
        assertThrows(IllegalArgumentException.class, () -> parser.parseConnection(null, buffer));
    }
}