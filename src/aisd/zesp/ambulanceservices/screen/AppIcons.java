package aisd.zesp.ambulanceservices.screen;

import javafx.scene.image.Image;

import java.io.File;

public class AppIcons {
    static final Image hospital = new Image(new File("resources/icons/icon_hospital.png").toURI().toString());
    static final Image hospitalFull = new Image(new File("resources/icons/icon_hospitalfull.png").toURI().toString());
    static final Image monument = new Image(new File("resources/icons/icon_monument.png").toURI().toString());
    static final Image patientAbandoned = new Image(new File("resources/icons/icon_patientabandon.png").toURI().toString());
    static final Image patientOK = new Image(new File("resources/icons/icon_patientok.png").toURI().toString());
    static final Image patientRiding = new Image(new File("resources/icons/icon_patientride.png").toURI().toString());
    static final Image patientWaiting = new Image(new File("resources/icons/icon_patientwaiting.png").toURI().toString());
    static final Image pause = new Image(new File("resources/icons/icon_pause.png").toURI().toString());
    static final Image play = new Image(new File("resources/icons/icon_play.png").toURI().toString());
    static final Image step = new Image(new File("resources/icons/icon_step.png").toURI().toString());
}
