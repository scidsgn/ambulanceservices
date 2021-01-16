package aisd.zesp.ambulanceservices.main;

import aisd.zesp.ambulanceservices.geometry.Point;

public class Landmark extends Point {
    private final int id;
    private final String name;

    public Landmark(int id, String name, double x, double y) {
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
