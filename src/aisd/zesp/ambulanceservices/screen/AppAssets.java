package aisd.zesp.ambulanceservices.screen;

import javafx.scene.image.Image;

import java.io.File;

public class AppAssets {
    public static final Image hospital = loadIcon("hospital");
    public static final Image hospitalFull = loadIcon("hospitalfull");
    public static final Image monument = loadIcon("monument");
    public static final Image patientAbandoned = loadIcon("patientabandon");
    public static final Image patientOK = loadIcon("patientok");
    public static final Image patientRiding = loadIcon("patientride");
    public static final Image patientWaiting = loadIcon("patientwaiting");
    public static final Image pause = loadIcon("pause");
    public static final Image play = loadIcon("play");
    public static final Image step = loadIcon("step");

    public static Image loadIcon(String name) {
        return new Image(new File("resources/icons/icon_" + name + ".png").toURI().toString());
    }
}
