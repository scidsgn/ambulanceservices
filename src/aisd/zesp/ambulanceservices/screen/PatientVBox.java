package aisd.zesp.ambulanceservices.screen;


import aisd.zesp.ambulanceservices.main.*;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import java.io.File;

import static aisd.zesp.ambulanceservices.main.PatientState.*;

public class PatientVBox extends VBox {

    private final ProgramAlgorithm programAlgorithm;

    public PatientVBox(ProgramAlgorithm programAlgorithm) {
        this.programAlgorithm = programAlgorithm;
    }

    public void showPatient() {


        PatientState patientState = WAITING;
        String state = null;

        getChildren().clear();
        Text tx = new Text(" Pacjenci");
        tx.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.ITALIC, 12));
        getChildren().addAll(tx);


        for (Patient p : programAlgorithm.getState().getPatientList()) {

            patientState = p.getPatientState();

            Image img = new Image(new File("icons/icon_patientwaiting.png").toURI().toString());
            ImageView view = new ImageView(img);

            Image imgAbandon = new Image(new File("icons/icon_patientabandon.png").toURI().toString());
            ImageView viewAbandon = new ImageView(imgAbandon);

            Image imgRide = new Image(new File("icons/icon_patientride.png").toURI().toString());
            ImageView viewRide = new ImageView(imgRide);

            Image imgOk = new Image(new File("icons/icon_patientok.png").toURI().toString());
            ImageView viewOk = new ImageView(imgOk);


            if (patientState == WAITING) {
                state = "Oczekuje na karetkę";
            } else if (patientState == RIDING || patientState == REJECTED) {
                state = "W trakcie jazdy";
                view = viewRide;
            } else if (patientState == OUTOFBOUNDS) {
                state = "Poza obszarem";
                view = viewAbandon;
            } else if (patientState == ABANDONED) {
                state = "Porzucony";
                view = viewAbandon;
            } else if (patientState == ACCEPTED) {
                state = "Hospitalowany w szpitalu";
                view = viewOk;
            }

            Label label = new Label(p.getName() + "    " + state + "    " + "P" + p.getId());

            label.setGraphic(view);

            this.getChildren().addAll(label);
        }
    }

}

