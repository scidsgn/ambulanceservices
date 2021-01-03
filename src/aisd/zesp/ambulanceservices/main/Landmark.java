package aisd.zesp.ambulanceservices.main;

import aisd.zesp.ambulanceservices.geometry.Point;

public class Landmark extends Point {
    private int id;
    private String name;

    public Landmark(int id, double x, double y, String name) {
        super(x, y);
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
