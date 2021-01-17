package aisd.zesp.ambulanceservices.screen;

import aisd.zesp.ambulanceservices.main.*;
import javafx.geometry.Insets;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.*;

import static aisd.zesp.ambulanceservices.main.PatientState.*;

public class InformationVBox extends VBox {
    private final ProgramAlgorithm programAlgorithm;

    private final Text infoMainText = new Text();
    private final Text infoSubText = new Text();

    public InformationVBox(ProgramAlgorithm programAlgorithm) {
        this.programAlgorithm = programAlgorithm;

        create();
    }

    private void create() {
        setPadding(new Insets(4, 12, 4, 12));

        infoMainText.setId("informationMain");
        infoMainText.setWrappingWidth(350);
        infoSubText.setId("informationSub");
        infoSubText.setWrappingWidth(350);

        this.getChildren().addAll(infoSubText, infoMainText);
    }

    public void showErrorMessage(String errorMessage) {
        infoMainText.setText(errorMessage);
        infoSubText.setText("Błąd");

        infoMainText.setFill(Color.WHITE);
        infoSubText.setFill(Color.WHITE);

        setBackground(new Background(new BackgroundFill(Color.DARKRED, CornerRadii.EMPTY, Insets.EMPTY)));
    }

    public void showInfoMessage(String message) {
        infoMainText.setText(message);
        infoSubText.setText("");

        infoMainText.setFill(Color.WHITE);
        infoSubText.setFill(Color.WHITE);

        setBackground(new Background(new BackgroundFill(Color.color(79 / 255., 79 / 255., 79 / 255.), CornerRadii.EMPTY, Insets.EMPTY)));
    }

    public void showInformation(Color bgColor) {
        String information = "";

        Patient patient = programAlgorithm.getCurrentPatient();
        PatientState patientState = patient.getPatientState();

        infoMainText.setFill(Color.WHITE);
        infoSubText.setFill(Color.WHITE);

        setBackground(new Background(new BackgroundFill(bgColor, CornerRadii.EMPTY, Insets.EMPTY)));

        if (patientState == WAITING) {
            information = "Pacjent oczekuje na karetkę";
        } else if (patientState == RIDING) {
            information = "Pacjent jedzie do szpitala S" + patient.getNearestHospital().getId();
        } else if (patientState == OUTOFBOUNDS) {
            information = "Pacjent znajduje się poza obsługiwanym\nobszarem";

            infoMainText.setFill(Color.RED);
            infoSubText.setFill(Color.RED);
        } else if (patientState == REJECTED) {
            information = "Pacjent nie został przyjęty w szpitalu S" + patient.getNearestHospital().getId();
        } else if (patientState == ABANDONED) {
            information = "Pacjent został porzucony";

            infoMainText.setFill(Color.RED);
            infoSubText.setFill(Color.RED);
        } else if (patientState == ACCEPTED) {
            information = "Pacjent został przyjęty w szpitalu S" + patient.getPatientHospital().getId();
        }

        infoMainText.setText(information);
        infoSubText.setText("P" + patient.getId() + " - " + patient.getName());
    }
}


