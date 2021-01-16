package aisd.zesp.ambulanceservices.screen;

import aisd.zesp.ambulanceservices.main.Hospital;
import aisd.zesp.ambulanceservices.main.ProgramAlgorithm;
import aisd.zesp.ambulanceservices.screen.AppIcons;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import javafx.scene.paint.Color;
import javafx.scene.text.*;


public class HospitalTableView extends VBox {


    private TableView<LHospital> table = new TableView<LHospital>();

    private final Color patientAndHospitalVBoxesBackground = Color.color(24 / 255., 24 / 255., 24 / 255.);
    private final ObservableList<LHospital> data = FXCollections.observableArrayList();


    private final ProgramAlgorithm programAlgorithm;

    public HospitalTableView(ProgramAlgorithm programAlgorithm) {
        this.programAlgorithm = programAlgorithm;
    }


    public void showHospital() {

        setBackground(new Background(new BackgroundFill(patientAndHospitalVBoxesBackground,
                CornerRadii.EMPTY,
                Insets.EMPTY)));
        Text txt = new Text("Szpitale");
        txt.setTextAlignment(TextAlignment.CENTER);

        table.setEditable(false);
        table.setStyle("-fx-background-color: #242424 ");
        txt.setFont(Font.font("verdana", FontWeight.BLACK, FontPosture.REGULAR, 15));


        TableColumn imageViewCol = new TableColumn("znak");
        imageViewCol.setMaxWidth(35);
        imageViewCol.setStyle("-fx-background-color: #242424 ;"
                + "-fx-border-color: #242424 ");
        imageViewCol.setCellValueFactory(
                new PropertyValueFactory<LHospital, String>("imageView"));


        TableColumn nameCol = new TableColumn("Imię i nazwisko");
        nameCol.setMinWidth(160);
        nameCol.setStyle("-fx-background-color: #242424 ;"
                + "-fx-border-color: #242424 ");
        nameCol.setCellValueFactory(
                new PropertyValueFactory<LHospital, String>("name"));


        TableColumn stateCol = new TableColumn("Stan pacjenta");
        stateCol.setMinWidth(160);
        stateCol.setStyle("-fx-background-color: #242424 ;"
                + "-fx-border-color: #242424 ");
        stateCol.setCellValueFactory(
                new PropertyValueFactory<LHospital, String>("vacantBeds"));


        TableColumn idCol = new TableColumn("Id");
        idCol.setMinWidth(45);
        idCol.setStyle("-fx-background-color: #242424 ;"
                + "-fx-border-color: #242424 ");
        idCol.setCellValueFactory(
                new PropertyValueFactory<LHospital, String>("id"));

        table.setItems(data);
        table.getColumns().addAll(imageViewCol, nameCol, stateCol, idCol);
        table.setTableMenuButtonVisible(false);


        getChildren().addAll(txt, table);


    }

    public void refreshHospitalslist() {

        table.getItems().clear();

        data.clear();
        getChildren().clear();

        setBackground(new Background(new BackgroundFill(patientAndHospitalVBoxesBackground,
                CornerRadii.EMPTY,
                Insets.EMPTY)));
        Text txt = new Text("Szpitale");
        txt.setFont(Font.font("verdana", FontWeight.BLACK, FontPosture.REGULAR, 15));
        txt.setTextAlignment(TextAlignment.CENTER);

        int vacantBeds = 0;

        for (Hospital h : programAlgorithm.getState().getHospitalList()) {

            vacantBeds = h.getVacantBeds();

            ImageView viewHospital = new ImageView(AppIcons.hospital);
            ImageView viewFullHospital = new ImageView(AppIcons.hospitalFull);
            ImageView view = viewHospital;


            if (vacantBeds == 0) {

                view = viewFullHospital;

            }


            LHospital person = new LHospital(view, h.getName(), vacantBeds, "S" + h.getId());
            data.add(person);


        }

        table.setItems(data);

        getChildren().addAll(txt, table);


    }


    public class LHospital {

        private ImageView imageView;
        private final SimpleStringProperty name;
        private final SimpleIntegerProperty vacantBeds;
        private final SimpleStringProperty id;


        private LHospital(ImageView imageView, String name, int vacantBeds, String id) {
            this.imageView = imageView;
            this.name = new SimpleStringProperty(name);
            this.vacantBeds = new SimpleIntegerProperty(vacantBeds);
            this.id = new SimpleStringProperty(id);
        }


        public ImageView getImageView() {
            return imageView;
        }

        public void setImageView(ImageView imageView) {
            this.imageView = imageView;

        }

        public String getName() {
            return name.get();
        }

        public void setName(String name) {
            this.name.set(name);
        }

        public int getVacantBeds() {
            return vacantBeds.get();
        }

        public void setVacantBeds(int state) {
            this.vacantBeds.set(state);
        }

        public String getId() {
            return id.get();
        }

        public void setId(String id) {
            this.id.set(id);
        }


    }
}