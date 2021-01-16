package aisd.zesp.ambulanceservices.main;

import aisd.zesp.ambulanceservices.geometry.Point;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Patient extends Point {
    private final int id;
    private String name;
    private PatientState patientState = PatientState.WAITING;
    private Hospital patientHospital = null;
    private Hospital nearestHospital = null;


    public Patient(int id, double x, double y) {
        super(x, y);
        this.id = id;
        generateName();
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public PatientState getPatientState() {
        return patientState;
    }

    public void setPatientState(PatientState patientState) {
        this.patientState = patientState;
    }

    private void generateName() {
        Random random = new Random();
        List<String> firstNames = new ArrayList<>();
        List<String> surNames = new ArrayList<>();

        firstNames.add("Lady");
        firstNames.add("Olga");
        firstNames.add("Piotr");
        firstNames.add("Mordzia");
        firstNames.add("Mariaantonina");
        firstNames.add("Jacuś");
        firstNames.add("Dżessika");
        firstNames.add("Krzesimir");
        firstNames.add("Bdzigost");
        firstNames.add("Brajan");
        firstNames.add("Ekscel");
        firstNames.add("Mortynka");

        surNames.add("Gronkowiec");
        surNames.add("Kovidek");
        surNames.add("Kowalski");
        surNames.add("Pałerpojnt");
        surNames.add("Martini");
        surNames.add("Eliminacjagaussa");
        surNames.add("Syntezatormowyivonawwersjidemonstracyjnej");
        surNames.add("Gaga");
        surNames.add("Meaculpa");
        surNames.add("Plac Politechniki Gmach Główny Sala 216");
        surNames.add("Konserwator Powierzchni Płaskich");
        surNames.add("Schmidt");

        String firstName = firstNames.get(random.nextInt(firstNames.size() - 1));
        String surName = surNames.get(random.nextInt(surNames.size() - 1));
        name = firstName + " " + surName;
    }


    public Hospital getPatientHospital() {
        return patientHospital;
    }

    public void setPatientHospital(Hospital currentHospital) {
        this.patientHospital = currentHospital;
    }

    public Hospital getNearestHospital() {
        return nearestHospital;
    }

    public void setNearestHospital(Hospital currentHospital) {
        this.nearestHospital = currentHospital;
    }
}
