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

            ImageView view = new ImageView(AppIcons.patientWaiting);
            ImageView viewAbandon = new ImageView(AppIcons.patientAbandoned);
            ImageView viewRide = new ImageView(AppIcons.patientRiding);
            ImageView viewOk = new ImageView(AppIcons.patientOK);


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

