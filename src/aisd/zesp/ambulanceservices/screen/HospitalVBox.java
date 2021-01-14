package aisd.zesp.ambulanceservices.screen;


import aisd.zesp.ambulanceservices.main.Hospital;
import aisd.zesp.ambulanceservices.main.ProgramAlgorithm;
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

            ImageView viewHospital = new ImageView(AppIcons.hospital);
            ImageView viewFullHospital = new ImageView(AppIcons.hospitalFull);


            Label label = new Label("S" + h.getId() + "     " + h.getName() + "    " + vacantBeds + "    " + "S" + h.getId());


            if (vacantBeds != 0) {
                label.setGraphic(viewHospital);
            } else {
                label.setGraphic(viewFullHospital);
            }

            this.getChildren().addAll(label);


        }
    }

}
