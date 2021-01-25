package aisd.zesp.ambulanceservices.screen;

import aisd.zesp.ambulanceservices.main.Patient;
import aisd.zesp.ambulanceservices.main.PatientState;
import aisd.zesp.ambulanceservices.main.ProgramAlgorithm;

import static aisd.zesp.ambulanceservices.main.PatientState.*;
import static aisd.zesp.ambulanceservices.main.PatientState.ACCEPTED;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class PatientTableView extends VBox {
    private TableView<LPatient> table = new TableView<>();
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

        Label txt = new Label("PACJENCI");

        TableColumn<LPatient, String> imageViewCol = new TableColumn<>("");
        imageViewCol.setMinWidth(24);
        imageViewCol.setMaxWidth(24);
        imageViewCol.setCellValueFactory(new PropertyValueFactory<>("imageView"));

        TableColumn<LPatient, String> nameCol = new TableColumn<>("Imię i nazwisko");
        nameCol.prefWidthProperty().bind(table.widthProperty().subtract(24).multiply(0.6).subtract(3));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<LPatient, String> stateCol = new TableColumn<>("Stan pacjenta");
        stateCol.prefWidthProperty().bind(table.widthProperty().subtract(24).multiply(0.3));
        stateCol.setCellValueFactory(new PropertyValueFactory<>("state"));

        TableColumn<LPatient, String> idCol = new TableColumn<>("Id");
        idCol.prefWidthProperty().bind(table.widthProperty().subtract(24).multiply(0.1));
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));

        table.setItems(data);
        table.getColumns().addAll(imageViewCol, nameCol, stateCol, idCol);
        table.setEditable(false);

        getChildren().addAll(txt, table);
    }

    public void refreshPatientsList() {
        table.getItems().clear();
        data.clear();

        for (Patient p : programAlgorithm.getState().getPatientList()) {
            PatientState patientState = p.getPatientState();
            String state = null;

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

            LPatient person = new LPatient(view, p.getName(), state, "P" + p.getId());
            data.add(person);
        }
        table.setItems(data);
    }


    public static class LPatient {
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
