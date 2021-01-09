package aisd.zesp.ambulanceservices.main;

import aisd.zesp.ambulanceservices.graham.ConvexHull;

import java.util.ArrayList;
import java.util.List;

public class ProgramAlgorithm {
    private State state = null;
    private Patient currentPatient;
    private List<Hospital> visitedHospitals;
    private Hospital currentHospital;

    private void selectPatient(Patient patient) {
        currentPatient = patient;
        visitedHospitals = new ArrayList<>();
        currentHospital = null;
    }

    private void goToFirstHospital() {
        // todo
    }

    private void goToNextHospital() {
        Hospital nextHospital = state.getNextHospital(currentHospital);

        if (nextHospital.getVacantBeds() > 0) {
            visitedHospitals.add(nextHospital);
            currentHospital = nextHospital;
        } else {
            currentPatient.setPatientState(PatientState.ABANDONED);
        }
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }
}
