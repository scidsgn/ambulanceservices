package aisd.zesp.ambulanceservices.main;

import aisd.zesp.ambulanceservices.graham.ConvexHull;

import java.util.ArrayList;
import java.util.List;

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

        System.out.println("PACJENT: " + currentPatient.getName());
    }

    private boolean checkPatientInBounds() {
        if (state.getConvexHull().isPointInHull(currentPatient)) {
            System.out.println("wewnątrz otoczki");
            return true;
        }

        System.out.println("poza otoczką");
        currentPatient.setPatientState(PatientState.OUTOFBOUNDS);

        return false;
    }

    private boolean goToHospital() {
        if (currentHospital == null) {
            Hospital hospital = state.findNearestHospital(currentPatient);

            visitedHospitals.add(hospital);
            currentHospital = hospital;
        } else {
            Hospital nextHospital = state.getNextHospital(currentHospital, visitedHospitals);

            if (nextHospital == null) {
                currentPatient.setPatientState(PatientState.ABANDONED);
                System.out.println("porzucony");

                return false;
            }

            visitedHospitals.add(nextHospital);
            currentHospital = nextHospital;
        }

        currentPatient.setPatientState(PatientState.RIDING);
        System.out.println("szpital: " + currentHospital.getName());

        return true;
    }

    private void checkHospitalCapacity() {
        if (currentHospital.getVacantBeds() > 0) {
            currentHospital.subtractOneBed();
            currentPatient.setPatientState(PatientState.ACCEPTED);
            System.out.println("zaakceptowany");
        } else {
            currentPatient.setPatientState(PatientState.REJECTED);
            System.out.println("odrzucony");
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
