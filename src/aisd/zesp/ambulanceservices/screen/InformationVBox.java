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


    public InformationVBox(ProgramAlgorithm programAlgorithm) {
        this.programAlgorithm = programAlgorithm;
    }

    public void showInformation() {

        getChildren().clear();

        String information = null;

        Patient patient = programAlgorithm.getCurrentPatient();
        PatientState patientState = patient.getPatientState();

        if (patientState == WAITING) {
            setBackground(new Background(new BackgroundFill(waitingBackground,
                    CornerRadii.EMPTY,
                    Insets.EMPTY)));
            information = "\nPacjent oczekuje \nna karetkę";
        } else if (patientState == RIDING) {
            setBackground(new Background(new BackgroundFill(ridingBackground,
                    CornerRadii.EMPTY,
                    Insets.EMPTY)));
            information = "\nPacjent jedzie \ndo szpitala S " + patient.getNearestHospital().getId();

        } else if (patientState == OUTOFBOUNDS) {
            setBackground(new Background(new BackgroundFill(abandonedBackground,
                    CornerRadii.EMPTY,
                    Insets.EMPTY)));
            information = "\nPacjent znajduje się poza\nobsługiwanym obszarem";

        } else if (patientState == REJECTED) {
            setBackground(new Background(new BackgroundFill(rejectedBackground,
                    CornerRadii.EMPTY,
                    Insets.EMPTY)));
            information = "\nPacjent nie został \nprzyjęty w szpitalu S " + patient.getNearestHospital().getId();

        } else if (patientState == ABANDONED) {
            setBackground(new Background(new BackgroundFill(abandonedBackground,
                    CornerRadii.EMPTY,
                    Insets.EMPTY)));
            information = "\nPacjent został \nporzucony";

        } else if (patientState == ACCEPTED) {
            setBackground(new Background(new BackgroundFill(acceptedBackground,
                    CornerRadii.EMPTY,
                    Insets.EMPTY)));
            information = "\nPacjent został \nprzyjęty w szpitalu S" + patient.getPatientHospital().getId();

        }


        Text tx1 = new Text(information);
        tx1.setTextAlignment(TextAlignment.JUSTIFY);
        tx1.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 30));


        Text tx2 = new Text("P" + patient.getId() + "-" + patient.getName());
        tx2.setTextAlignment(TextAlignment.LEFT);
        tx2.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 12));
        tx2.setTranslateY(80);

        this.getChildren().addAll(tx1,tx2);
    }
}


