package aisd.zesp.ambulanceservices.screen;


import aisd.zesp.ambulanceservices.main.Patient;
import aisd.zesp.ambulanceservices.main.PatientState;
import aisd.zesp.ambulanceservices.main.ProgramAlgorithm;
import aisd.zesp.ambulanceservices.screen.AppIcons;

import static aisd.zesp.ambulanceservices.main.PatientState.*;
import static aisd.zesp.ambulanceservices.main.PatientState.ACCEPTED;
import static javafx.application.Application.launch;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.*;


public class PatientTableView extends VBox {


    private TableView<LPatient> table = new TableView<LPatient>();


    private final ObservableList<LPatient> data = FXCollections.observableArrayList();

    private final Color patientAndHospitalVBoxesBackground = Color.color(24 / 255., 24 / 255., 24 / 255.);

    private final ProgramAlgorithm programAlgorithm;

    public PatientTableView(ProgramAlgorithm programAlgorithm) {
        this.programAlgorithm = programAlgorithm;
    }

    public void showPatient() {

        setBackground(new Background(new BackgroundFill(patientAndHospitalVBoxesBackground,
                CornerRadii.EMPTY,
                Insets.EMPTY)));
        Text txt = new Text("Pacjenci");
        txt.setFont(Font.font("verdana", FontWeight.BLACK, FontPosture.REGULAR, 15));
        txt.setTextAlignment(TextAlignment.CENTER);

        table.setEditable(false);
        table.setStyle("-fx-background-color: #242424");


        TableColumn imageViewCol = new TableColumn("znak");
        imageViewCol.setMaxWidth(35);
        imageViewCol.setStyle("-fx-background-color: #242424 ;"
                + "-fx-border-color: #242424 ");
        imageViewCol.setCellValueFactory(
                new PropertyValueFactory<LPatient, String>("imageView"));

        TableColumn nameCol = new TableColumn("Imię i nazwisko");
        nameCol.setMinWidth(160);
        nameCol.setStyle("-fx-background-color: #242424 ;"
                + "-fx-border-color:#242424 ");
        nameCol.setCellValueFactory(
                new PropertyValueFactory<LPatient, String>("name"));


        TableColumn stateCol = new TableColumn("Stan pacjenta");
        stateCol.setMinWidth(160);
        stateCol.setStyle("-fx-background-color: #242424 ;"
                + "-fx-border-color: #242424 ");
        stateCol.setCellValueFactory(
                new PropertyValueFactory<LPatient, String>("state"));


        TableColumn idCol = new TableColumn("Id");
        idCol.setMinWidth(45);
        idCol.setStyle("-fx-background-color:#242424 ;"
                + "-fx-border-color:#242424");
        idCol.setCellValueFactory(
                new PropertyValueFactory<LPatient, String>("id"));

        table.setItems(data);
        table.getColumns().addAll(imageViewCol, nameCol, stateCol, idCol);

        getChildren().addAll(txt, table);


    }

    public void refreshPatientslist() {

        setBackground(new Background(new BackgroundFill(patientAndHospitalVBoxesBackground,
                CornerRadii.EMPTY,
                Insets.EMPTY)));
        Text txt = new Text("Pacjenci");
        txt.setTextAlignment(TextAlignment.CENTER);
        txt.setFont(Font.font("verdana", FontWeight.BLACK, FontPosture.REGULAR, 15));

        table.getItems().clear();

        data.clear();
        getChildren().clear();


        for (Patient p : programAlgorithm.getState().getPatientList()) {

            PatientState patientState = p.getPatientState();
            String state = null;


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
                state = "Hospitalowany w szpitalu S " + p.getPatientHospital().getId();
                view = viewOk;
            }

            LPatient person = new LPatient(view, p.getName(), state, "P" + p.getId());
            data.add(person);
        }
        table.setItems(data);

        getChildren().addAll(txt, table);


    }


    public class LPatient {

        private ImageView imageView;
        private final SimpleStringProperty name;
        private final SimpleStringProperty state;
        private final SimpleStringProperty id;


        private LPatient(ImageView imageView, String name, String state, String id) {
            this.imageView = imageView;
            this.name = new SimpleStringProperty(name);
            this.state = new SimpleStringProperty(state);
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

        public String getState() {
            return state.get();
        }

        public void setState(String state) {
            this.state.set(state);
        }

        public String getId() {
            return id.get();
        }

        public void setId(String id) {
            this.id.set(id);
        }


    }

}
