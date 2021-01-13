package aisd.zesp.ambulanceservices.screen;


import aisd.zesp.ambulanceservices.main.*;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;


import static aisd.zesp.ambulanceservices.main.PatientState.*;

public class InformationVBox extends VBox {

    private final ProgramAlgorithm programAlgorithm;


    public InformationVBox(ProgramAlgorithm programAlgorithm) {
        this.programAlgorithm = programAlgorithm;
    }

    public void showInformation() {

        getChildren().clear();

        String information = null;

        Patient patient = programAlgorithm.getCurrentPatient();
        PatientState patientState = patient.getPatientState();
        if (patientState == WAITING) {
            information = "Oczekuje na karetkę";
        } else if (patientState == RIDING ) {
            information = "W trakcie jazdy \n do szpitala S " + patient.getNearestHospital().getId();

        } else if (patientState == OUTOFBOUNDS) {
            information = "Poza obszarem państwa";

        }  else if ( patientState == REJECTED) {
            information = "Nie został przyjęty\n w szpitalu S " + patient.getNearestHospital().getId();

        } else if (patientState == ABANDONED) {
            information = "Porzucony";

        } else if (patientState == ACCEPTED) {
            information = "Hospitalowany \n w szpitalu S " + patient.getPatientHospital().getId();

        }


        Text tx1 = new Text(information);
        tx1.setLayoutY(30);
        tx1.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.ITALIC, 30));

        this.getChildren().addAll(tx1);
    }
}


