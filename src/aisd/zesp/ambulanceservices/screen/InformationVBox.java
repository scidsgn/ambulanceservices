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
    private final Color ridingBackground = Color.color(29/255., 107/255., 150/255.);
    private final Color waitingBackground = Color.color(79/255., 79/255., 79/255.);
    private final Color rejectedBackground = Color.color(93/255., 60/255., 124/255.);
    private final Color acceptedBackground = Color.color(48/255., 136/255., 60/255.);
    private final Color abandonedBackground = Color.color(14/255., 15/255., 15/255.);

    private final Text infoMainText = new Text();
    private final Text infoSubText = new Text();

    public InformationVBox(ProgramAlgorithm programAlgorithm) {
        this.programAlgorithm = programAlgorithm;

        create();
    }

    private void create() {
        infoMainText.setId("informationMain");

        infoSubText.setId("informationSub");
        infoSubText.setTranslateY(80);

        this.getChildren().addAll(infoMainText, infoSubText);
    }

    public void showInformation() {
        String information = "";

        Patient patient = programAlgorithm.getCurrentPatient();
        PatientState patientState = patient.getPatientState();

        infoMainText.setFill(Color.WHITE);
        infoSubText.setFill(Color.WHITE);

        if (patientState == WAITING) {
            setBackground(new Background(new BackgroundFill(waitingBackground,
                    CornerRadii.EMPTY,
                    Insets.EMPTY)));
            information = "Pacjent oczekuje na karetkę";
        } else if (patientState == RIDING) {
            setBackground(new Background(new BackgroundFill(ridingBackground,
                    CornerRadii.EMPTY,
                    Insets.EMPTY)));
            information = "Pacjent jedzie do szpitala S " + patient.getNearestHospital().getId();

        } else if (patientState == OUTOFBOUNDS) {
            setBackground(new Background(new BackgroundFill(abandonedBackground,
                    CornerRadii.EMPTY,
                    Insets.EMPTY)));
            information = "Pacjent znajduje się poza obsługiwanym\nobszarem";

            infoMainText.setFill(Color.RED);
            infoSubText.setFill(Color.RED);

        } else if (patientState == REJECTED) {
            setBackground(new Background(new BackgroundFill(rejectedBackground,
                    CornerRadii.EMPTY,
                    Insets.EMPTY)));
            information = "Pacjent nie został przyjęty w szpitalu S " + patient.getNearestHospital().getId();

        } else if (patientState == ABANDONED) {
            setBackground(new Background(new BackgroundFill(abandonedBackground,
                    CornerRadii.EMPTY,
                    Insets.EMPTY)));
            information = "Pacjent został porzucony";

            infoMainText.setFill(Color.RED);
            infoSubText.setFill(Color.RED);

        } else if (patientState == ACCEPTED) {
            setBackground(new Background(new BackgroundFill(acceptedBackground,
                    CornerRadii.EMPTY,
                    Insets.EMPTY)));
            information = "Pacjent został przyjęty w szpitalu S" + patient.getPatientHospital().getId();
        }

        infoMainText.setText(information);
        infoSubText.setText("P" + patient.getId() + " - " + patient.getName());
    }
}


