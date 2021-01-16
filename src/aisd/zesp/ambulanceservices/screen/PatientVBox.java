package aisd.zesp.ambulanceservices.screen;


import aisd.zesp.ambulanceservices.main.*;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;


import static aisd.zesp.ambulanceservices.main.PatientState.*;

public class PatientVBox extends VBox {

    private final ProgramAlgorithm programAlgorithm;

    public PatientVBox(ProgramAlgorithm programAlgorithm) {
        this.programAlgorithm = programAlgorithm;
    }

    public void showPatient() {


        PatientState patientState;
        String state = null;

        getChildren().clear();
        Text tx = new Text(" Pacjenci");
        tx.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.ITALIC, 12));
        getChildren().addAll(tx);


        for (Patient p : programAlgorithm.getState().getPatientList()) {

            patientState = p.getPatientState();


            ImageView view = new ImageView(AppAssets.patientWaiting);
            ImageView viewAbandon = new ImageView(AppAssets.patientAbandoned);
            ImageView viewRide = new ImageView(AppAssets.patientRiding);
            ImageView viewOk = new ImageView(AppAssets.patientOK);


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
                state = "Hospitalowany w szpitalu S " + p.getPatientHospital().getId();
                view = viewOk;
            }
            Label label = new Label(p.getName() + "    " + state + "    " + "P" + p.getId());

            label.setGraphic(view);

            setBackground(new Background(new BackgroundFill(Color.DIMGRAY, CornerRadii.EMPTY, Insets.EMPTY)));
            setPrefWidth(620);
            setPrefHeight(300);
            setStyle("-fx-border-style: solid;" + "-fx-border-width: 0;" + "-fx-border-color: black");

            this.getChildren().addAll(label);
        }
    }

}

