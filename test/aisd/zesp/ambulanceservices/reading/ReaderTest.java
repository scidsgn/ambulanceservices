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
    private Hospital testHospital;
    private Landmark testLandmark;
    private State testState;
    private Patient testPatient;

    @BeforeEach
    void setUp() {
        reader = new Reader();
    }

    @Test
    void shouldNotAllowWrongPathWithLoad() {
        assertThrows(IllegalArgumentException.class, () -> reader.load("HURR DURR ERROR"));
    }

    @Test
    void shouldNotAllowWrongPathWithLoadPatients() {
        State state = new State();

        assertThrows(IllegalArgumentException.class, () -> reader.loadPatients(state,"HURR DURR ERROR"));
    }

    @Test
    void stateCannotBeNull() {
        assertThrows(NullPointerException.class, () -> reader.loadPatients(null, "data.txt"));
    }

    @Test
    void doesLoadChangeState() {
        testState = reader.load("data.txt");
        testHospital = new Hospital(1, "Szpital Wojewódzki nr 997", 10, 10, 100);
        testLandmark = new Landmark(1, "Pomnik Wikipedii", -1, 50);

        assertEquals(testState.getHospitalById(1).getId(), testHospital.getId());
        assertEquals(testState.getHospitalById(1).getName(), testHospital.getName());
        assertEquals(testState.getHospitalById(1).getX(), testHospital.getX());
        assertEquals(testState.getHospitalById(1).getY(), testHospital.getY());
        assertEquals(testState.getHospitalById(1).getVacantBeds(), testHospital.getVacantBeds());

        assertEquals(testState.getLandmarkById(1).getId(), testLandmark.getId());
        assertEquals(testState.getLandmarkById(1).getName(), testLandmark.getName());
        assertEquals(testState.getLandmarkById(1).getX(), testLandmark.getX());
        assertEquals(testState.getLandmarkById(1).getY(), testLandmark.getY());
    }

    @Test
    void doesLoadPatientChangeState() {
        testState = reader.load("data.txt");
        testPatient = new Patient(1,20, 20);

        reader.loadPatients(testState,"patients.txt");

        assertEquals(testState.getPatientById(1).getId(),testPatient.getId());
        assertEquals(testState.getPatientById(1).getX(),testPatient.getX());
        assertEquals(testState.getPatientById(1).getY(),testPatient.getY());
    }
}