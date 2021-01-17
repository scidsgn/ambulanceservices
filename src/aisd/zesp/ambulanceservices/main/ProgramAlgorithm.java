package aisd.zesp.ambulanceservices.main;

import java.util.*;

public class ProgramAlgorithm {
    private State state = null;

    private Patient currentPatient;
    private List<Hospital> visitedHospitals;
    private Hospital currentHospital;

    public Patient getCurrentPatient() {
        return currentPatient;
    }

    public Hospital getCurrentHospital() {
        return currentHospital;
    }

    private void selectPatient(Patient patient) {
        currentPatient = patient;

        visitedHospitals = new ArrayList<>();
        currentHospital = null;
    }

    private boolean checkPatientInBounds() {
        if (state.getConvexHull().isPointInHull(currentPatient)) {
            return true;
        }

        currentPatient.setPatientState(PatientState.OUTOFBOUNDS);

        return false;
    }

    private void goToHospital() {
        if (currentHospital == null) {
            Hospital hospital = state.findNearestHospital(currentPatient);

            visitedHospitals.add(hospital);
            currentHospital = hospital;
            currentPatient.setNearestHospital(currentHospital);
        } else {
            Hospital nextHospital = state.getNextHospital(currentHospital, visitedHospitals);

            if (nextHospital == null) {
                currentPatient.setPatientState(PatientState.ABANDONED);
            }

            visitedHospitals.add(nextHospital);
            currentHospital = nextHospital;
            currentPatient.setNearestHospital(currentHospital);
        }

        currentPatient.setPatientState(PatientState.RIDING);
    }

    private void checkHospitalCapacity() {
        if (currentHospital.getVacantBeds() > 0) {
            currentHospital.subtractOneBed();
            currentPatient.setPatientState(PatientState.ACCEPTED);
            currentPatient.setPatientHospital(currentHospital);

        } else {
            currentPatient.setPatientState(PatientState.REJECTED);
        }
    }

    public int nextStep() {
        PatientState currentState = (currentPatient == null) ? PatientState.ABANDONED : currentPatient.getPatientState();
        if (
                currentState == PatientState.ACCEPTED || currentState == PatientState.ABANDONED ||
                currentState == PatientState.OUTOFBOUNDS
        ) {
            Patient patient = state.getNextPatient();
            if (patient == null) {
                return 1;
            }
            selectPatient(patient);
        } else if (currentState == PatientState.WAITING) {
            if (checkPatientInBounds()) {
                goToHospital();
            }
        } else if (currentState == PatientState.REJECTED) {
            goToHospital();
        } else if (currentState == PatientState.RIDING) {
            checkHospitalCapacity();
        }
        return 0;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }
}
