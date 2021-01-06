package aisd.zesp.ambulanceservices.main;

import aisd.zesp.ambulanceservices.geometry.Point;

public class Hospital extends Point {
    private final int id;
    private final String name;
    private int vacantBeds;

    public Hospital(int id, String name, double x, double y,  int vacantBeds) {
        super(x, y);
        this.id = id;
        this.name = name;
        this.vacantBeds = vacantBeds;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getVacantBeds() {
        return vacantBeds;
    }

    public void subtractOneBed() {
        vacantBeds--;
    }
}
