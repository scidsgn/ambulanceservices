package aisd.zesp.ambulanceservices.screen;

import aisd.zesp.ambulanceservices.main.Patient;
import aisd.zesp.ambulanceservices.main.ProgramAlgorithm;
import aisd.zesp.ambulanceservices.main.State;
import aisd.zesp.ambulanceservices.reading.Reader;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;


public class ScreenView extends GridPane {

    private final Stage primaryStage;
    private final ProgramAlgorithm programAlgorithm;
    private final Reader reader = new Reader();
    private State state;

    private MapCanvas canvas;
    private HospitalVBox hospitalVBox;
    private PatientVBox patientVBox;
    private InformationVBox informationVBox;

    public ScreenView(Stage primaryStage, ProgramAlgorithm programAlgorithm) {
        this.primaryStage = primaryStage;
        this.programAlgorithm = programAlgorithm;
    }


    public void draw() {

        hospitalVBox = new HospitalVBox(programAlgorithm);
        patientVBox = new PatientVBox(programAlgorithm);
        informationVBox = new InformationVBox(programAlgorithm);


        HBox root = new HBox(0);
        root.setPrefHeight(800);
        root.setAlignment(Pos.CENTER);


        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File("data"));
        fileChooser.setTitle("Wybierz plik z rozszerzeniem *.txt");


        Button loadMap = new Button("Wczytaj mapę");
        loadMap.setFont(Font.font("verdana", FontWeight.BLACK, FontPosture.REGULAR, 12));

        loadMap.setOnAction(
                e -> {
                    if (programAlgorithm.getState() != null) {
                        // WYPISANIE ŻEBY NIE ŁADOWAĆ 2 RAZY MAPY
                        return;
                    }

                    fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("txt Files", "*.txt"));
                    File file = fileChooser.showOpenDialog(primaryStage);

                    try {
                        state = reader.load(file.getAbsolutePath());
                        state.finalizeConnections();
                        state.finalizeConvexHull();
                        programAlgorithm.setState(state);
                        canvas.draw();
                        hospitalVBox.showHospital();
                    } catch (IllegalArgumentException ex) {
                        Alerts.showAlert(ex.getMessage());
                    }
                }
        );

        Button loadPatientsList = new Button("Wczytaj listę pacjentów");
        loadPatientsList.setFont(Font.font("verdana", FontWeight.BLACK, FontPosture.REGULAR, 12));
        loadPatientsList.setOnAction(
                e -> {
                    if (programAlgorithm.getState() == null) {
                        // WYPISANIE ŻEBY NAJPIERW ZAŁADOWAĆ MAPĘ
                        return;
                    }

                    fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("txt Files", "*.txt"));
                    File file = fileChooser.showOpenDialog(primaryStage);

                    String errorMessage = null;
                    try {
                        reader.loadPatients(programAlgorithm.getState(), file.getAbsolutePath());
                        canvas.draw();
                        patientVBox.showPatient();
                    } catch (IllegalArgumentException ex) {
                        errorMessage = ex.getMessage();
                        Alerts.showAlert(errorMessage);
                    }
                }
        );

        Button simulationStepButton = new Button("dev krok symulacji");
        simulationStepButton.setFont(Font.font("verdana", FontWeight.BLACK, FontPosture.REGULAR, 12));
        simulationStepButton.setOnAction(
                e -> {
                    programAlgorithm.nextStep();
                    canvas.draw();
                    hospitalVBox.showHospital();
                    patientVBox.showPatient();
                    informationVBox.showInformation();

                }
        );

        loadMap.setTranslateX(5);


        HBox hbox = new HBox(10);
        hbox.setAlignment(Pos.CENTER_LEFT);

        hbox.getChildren().addAll(loadMap, loadPatientsList, simulationStepButton);


        VBox vbox = new VBox(0);
        vbox.setStyle("-fx-border-style: solid;"
                + "-fx-border-width: 1;"
                + "-fx-border-color: black");
        vbox.setAlignment(Pos.BASELINE_LEFT);
        vbox.setBackground(new Background(new BackgroundFill(Color.BLACK,
                CornerRadii.EMPTY,
                Insets.EMPTY)));

        vbox.getChildren().add(hbox);

        vbox.setPrefHeight(50);
        vbox.setPrefWidth(1000);
        VBox.setVgrow(vbox, Priority.ALWAYS);

        canvas = new MapCanvas(999, 755, programAlgorithm);
        vbox.getChildren().add(canvas);

        canvas.setOnMouseClicked(eventHandler);


        VBox vbox2 = new VBox(0);
        vbox2.setAlignment(Pos.CENTER_RIGHT);


        informationVBox.setBackground(new Background(new BackgroundFill(Color.DARKGRAY,
                CornerRadii.EMPTY,
                Insets.EMPTY)));
        informationVBox.setPrefWidth(620);
        informationVBox.setPrefHeight(300);
        informationVBox.setStyle("-fx-border-style: solid;"
                + "-fx-border-width: 2;"
                + "-fx-border-color: black");


        hospitalVBox.setBackground(new Background(new BackgroundFill(Color.DIMGRAY,
                CornerRadii.EMPTY,
                Insets.EMPTY)));
        hospitalVBox.setPrefWidth(620);
        hospitalVBox.setPrefHeight(300);
        hospitalVBox.setStyle("-fx-border-style: solid;"
                + "-fx-border-width: 0;"
                + "-fx-border-color: black");


        patientVBox.setBackground(new Background(new BackgroundFill(Color.DIMGRAY,
                CornerRadii.EMPTY,
                Insets.EMPTY)));
        patientVBox.setPrefWidth(620);
        patientVBox.setPrefHeight(300);
        patientVBox.setStyle("-fx-border-style: solid;"
                + "-fx-border-width: 0;"
                + "-fx-border-color: black");


        Button start = new Button("Start");
        start.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 12));

        start.setTranslateX(5);
        start.setTranslateY(3);


        Text tx2 = new Text(" Szpitale");
        tx2.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.ITALIC, 12));

        Text tx3 = new Text(" Pacjenci");
        tx3.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.ITALIC, 12));

        VBox.setVgrow(informationVBox, Priority.ALWAYS);
        VBox.setVgrow(hospitalVBox, Priority.ALWAYS);
        VBox.setVgrow(patientVBox, Priority.ALWAYS);


        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setBackground(new Background(new BackgroundFill(Color.BLACK,
                CornerRadii.EMPTY,
                Insets.EMPTY)));
        hospitalVBox.getChildren().addAll(tx2);
        scrollPane.setContent(hospitalVBox);

        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);


        ScrollPane scrollPane2 = new ScrollPane();
        patientVBox.getChildren().addAll(tx3);
        scrollPane2.setContent(patientVBox);
        scrollPane2.setBackground(new Background(new BackgroundFill(Color.BLACK,
                CornerRadii.EMPTY,
                Insets.EMPTY)));

        scrollPane2.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        scrollPane2.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);


        vbox2.getChildren().addAll(informationVBox, scrollPane, scrollPane2);

        root.getChildren().addAll(vbox, vbox2);
        Scene scene = new Scene(root, 1540, 900);

        this.add(root, 0, 0);


        primaryStage.setTitle("Ambulance services");
        primaryStage.setScene(scene);
        primaryStage.show();

        canvas.draw();
    }

    EventHandler<MouseEvent> eventHandler = new EventHandler<>() {
        @Override
        public void handle(MouseEvent e) {
            if (programAlgorithm.getState() != null) {
                GraphicsContext g = canvas.getGraphicsContext2D();
                Patient patient = state.addPatientFromCanvas(e.getX(), e.getY());
                canvas.drawPatient(g, patient);
            }
        }
    };
}
