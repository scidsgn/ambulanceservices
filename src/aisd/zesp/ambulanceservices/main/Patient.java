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

    private final List<String> firstNames;
    private final List<String> lastNames;

    public Patient(int id, double x, double y) {
        super(x, y);

        this.id = id;

        firstNames = new ArrayList<>();
        lastNames = new ArrayList<>();
        setUpNameLists();

        name = generateName();
    }

    private void setUpNameLists() {
        firstNames.add("Lady");
        firstNames.add("Olga");
        firstNames.add("Piotr");
        firstNames.add("Mariaantonina");
        firstNames.add("Jacuś");
        firstNames.add("Dżessika");
        firstNames.add("Krzesimir");
        firstNames.add("Bdzigost");
        firstNames.add("Brajan");
        firstNames.add("Ekscel");
        firstNames.add("Mortynka");

        lastNames.add("Kovidek");
        lastNames.add("Kowalski");
        lastNames.add("Pałerpojnt");
        lastNames.add("Martini");
        lastNames.add("Eliminacjagaussa");
        lastNames.add("Syntezatormowyivonawwersjidemonstracyjnej");
        lastNames.add("Gaga");
        lastNames.add("Meaculpa");
        lastNames.add("Plac Politechniki Gmach Główny Sala 216");
        lastNames.add("Konserwator Powierzchni Płaskich");
        lastNames.add("Schmidt");
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

    private String generateName() {
        Random random = new Random();

        String firstName = firstNames.get(random.nextInt(firstNames.size() - 1));
        String surName = lastNames.get(random.nextInt(lastNames.size() - 1));

        return firstName + " " + surName;
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
