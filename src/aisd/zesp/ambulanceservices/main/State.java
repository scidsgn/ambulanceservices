package aisd.zesp.ambulanceservices.main;

import java.util.ArrayList;
import java.util.List;

public class State {
    private final List<Hospital> hospitalList;
    private final List<Patient> patientList;
    private final List<Landmark> landmarkList;
    private int connectionNumber = 0;

    public State() {
        hospitalList = new ArrayList<>();
        patientList = new ArrayList<>();
        landmarkList = new ArrayList<>();
    }

    public Hospital getHospitalById(int id) {
        for (Hospital hospital : hospitalList) {
            if (hospital.getId() == id) {
                return hospital;
            }
        }

        return null;
    }

    public Patient getPatientById(int id) {
        for (Patient patient : patientList) {
            if (patient.getId() == id) {
                return patient;
            }
        }

        return null;
    }

    public void addPatient(Patient patient) {
        if  (patient == null) {
            throw new NullPointerException("Patient cannot be null.");
        }
        if (getPatientById(patient.getId()) != null) {
            throw new IllegalArgumentException("Patient with that ID already added.");
        }

        patientList.add(patient);
    }

    public void addHospital(Hospital hospital) throws IllegalArgumentException, NullPointerException {
        if  (hospital == null) {
            throw new NullPointerException("Hospital cannot be null.");
        }
        if (getHospitalById(hospital.getId()) != null) {
            throw new IllegalArgumentException("Hospital with that ID already added.");
        }

        hospitalList.add(hospital);
    }

    public Landmark getLandmarkById(int id) {
        for (Landmark landmark : landmarkList) {
            if(landmark.getId() == id){
                return landmark;
            }
        }

        return null;
    }

    public void addLandmark(Landmark landmark) {
        if  (landmark == null) {
            throw new NullPointerException("Landmark cannot be null.");
        }
        if (getLandmarkById(landmark.getId()) != null) {
            throw new IllegalArgumentException("Landmark with that ID already added.");
        }

        landmarkList.add(landmark);
    }

    public void addConnection(int id, Hospital fh, Hospital sh, double length) {
        if(connectionNumber > id) {
            throw new IllegalArgumentException("There already is connection with ID " + id);
        }

        connectionNumber++;
    }


}
