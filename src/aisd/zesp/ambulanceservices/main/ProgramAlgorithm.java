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

    private boolean checkPatientInBounds() {
        if (state.getConvexHull().isPointInHull(currentPatient)) {
            return true;
        }

        currentPatient.setPatientState(PatientState.OUTOFBOUNDS);

        return false;
    }

    private boolean goToHospital() {
        if (currentHospital == null) {
            Hospital hospital = state.findNearestHospital(currentPatient);

            visitedHospitals.add(hospital);
            currentHospital = hospital;
        } else {
            Hospital nextHospital = state.getNextHospital(currentHospital);

            if (visitedHospitals.contains(nextHospital)) {
                currentPatient.setPatientState(PatientState.ABANDONED);

                return false;
            }

            visitedHospitals.add(nextHospital);
            currentHospital = nextHospital;
        }

        currentPatient.setPatientState(PatientState.RIDING);

        return true;
    }

    private void checkHospitalCapacity() {
        if (currentHospital.getVacantBeds() > 0) {
            currentHospital.subtractOneBed();
            currentPatient.setPatientState(PatientState.ACCEPTED);
        } else {
            currentPatient.setPatientState(PatientState.REJECTED);
        }
    }

    public void nextStep() {
        PatientState currentState = (currentPatient == null) ? PatientState.ABANDONED : currentPatient.getPatientState();
        if (currentState == PatientState.ACCEPTED || currentState == PatientState.ABANDONED || currentState == PatientState.OUTOFBOUNDS) {
            selectPatient(state.getNextPatient());
        } else if (currentState == PatientState.WAITING) {
            if (checkPatientInBounds()) {
                goToHospital();
            }
        } else if (currentState == PatientState.REJECTED) {
            goToHospital();
        } else if (currentState == PatientState.RIDING) {
            checkHospitalCapacity();
        }
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }
}
