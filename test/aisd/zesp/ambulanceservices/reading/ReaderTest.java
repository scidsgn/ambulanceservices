package aisd.zesp.ambulanceservices.reading;

import aisd.zesp.ambulanceservices.main.Hospital;
import aisd.zesp.ambulanceservices.main.Landmark;
import aisd.zesp.ambulanceservices.main.Patient;
import aisd.zesp.ambulanceservices.main.State;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ReaderTest {
    private Reader reader;
    private State testState;

    @BeforeEach
    void setUp() {
        reader = new Reader();
        testState = reader.load("data/data.txt");
    }

    @Test
    void loadThrowsOnInvalidFile() {
        assertThrows(IllegalArgumentException.class, () -> reader.load("invalid.txt"));
    }

    @Test
    void loadPatientsThrowsOnInvalidFile() {
        State state = new State();

        assertThrows(IllegalArgumentException.class, () -> reader.loadPatients(state,"invalid.txt"));
    }

    @Test
    void loadPatientsThrowsOnNullState() {
        assertThrows(NullPointerException.class, () -> reader.loadPatients(null, "data.txt"));
    }

    @Test
    void loadSucceeds() {
        Hospital testHospital = new Hospital(1, "Szpital Wojewódzki nr 997", 10, 10, 1);
        Landmark testLandmark = new Landmark(1, "Pomnik Wikipedii", -1, 50);

        assertEquals(testHospital.getId(), testState.getHospitalById(1).getId());
        assertEquals(testHospital.getName(), testState.getHospitalById(1).getName());
        assertEquals(testHospital.getX(), testState.getHospitalById(1).getX());
        assertEquals(testHospital.getY(), testState.getHospitalById(1).getY());
        assertEquals(testHospital.getVacantBeds(), testState.getHospitalById(1).getVacantBeds());

        assertEquals(testLandmark.getId(), testState.getLandmarkById(1).getId());
        assertEquals(testLandmark.getName(), testState.getLandmarkById(1).getName());
        assertEquals(testLandmark.getX(), testState.getLandmarkById(1).getX());
        assertEquals(testLandmark.getY(), testState.getLandmarkById(1).getY());
    }

    @Test
    void loadPatientsSucceeds() {
        Patient testPatient = new Patient(1, 20, 20);

        reader.loadPatients(testState,"data/patients.txt");

        assertEquals(testPatient.getId(), testState.getPatientById(1).getId());
        assertEquals(testPatient.getX(), testState.getPatientById(1).getX());
        assertEquals(testPatient.getY(), testState.getPatientById(1).getY());
    }

    @Test
    void loadThrowsOnSecondAttempt() {
        assertThrows(IllegalArgumentException.class, () -> testState = reader.load("data/invalidHospitalData.txt"));
        assertThrows(IllegalArgumentException.class, () -> testState = reader.load("data/invalidLandmarkData.txt"));
        assertThrows(IllegalArgumentException.class, () -> testState = reader.load("data/invalidConnectionData.txt"));
    }

    @Test
    void loadPatientsThrowsOnSecondAttempt() {
        assertThrows(IllegalArgumentException.class, () -> reader.loadPatients(testState, "data/invalidPatients.txt"));
    }
}