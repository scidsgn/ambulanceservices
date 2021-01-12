package aisd.zesp.ambulanceservices.screen;


import aisd.zesp.ambulanceservices.main.Hospital;
import aisd.zesp.ambulanceservices.main.ProgramAlgorithm;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import java.io.File;

public class HospitalVBox extends VBox {

    private final ProgramAlgorithm programAlgorithm;

    public HospitalVBox(ProgramAlgorithm programAlgorithm) {
        this.programAlgorithm = programAlgorithm;
    }

    public void showHospital() {

        int vacantBeds = 0;

        getChildren().clear();
        Text tx = new Text(" Szpitale");
        tx.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.ITALIC, 12));
        getChildren().addAll(tx);


        for (Hospital h : programAlgorithm.getState().getHospitalList()) {

            vacantBeds = h.getVacantBeds();


            Image imgHospital = new Image(new File("icons/icon_hospital.png").toURI().toString());

            ImageView viewHospital = new ImageView(imgHospital);

            Image imgFullHospital = new Image(new File("icons/icon_hospitalfull.png").toURI().toString());
            ImageView viewFullHospital = new ImageView(imgFullHospital);


            Label label = new Label(h.getName() + "    " + vacantBeds + "    " + "S" + h.getId());


            if (vacantBeds != 0) {
                label.setGraphic(viewHospital);
            } else {
                label.setGraphic(viewFullHospital);
            }

            this.getChildren().addAll(label);

        }
    }

}
