package aisd.zesp.ambulanceservices.main;

import aisd.zesp.ambulanceservices.geometry.Point;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Patient extends Point {
    private final int id;
    private String name;

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

    private void generateName() {
        Random random = new Random();
        List <String> firstNames = new ArrayList<>();
        List <String> surNames = new ArrayList<>();

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
        surNames.add("Sasino");
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
}
