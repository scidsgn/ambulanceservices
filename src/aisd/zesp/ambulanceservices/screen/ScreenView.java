package aisd.zesp.ambulanceservices.screen;

import aisd.zesp.ambulanceservices.geometry.Point;
import aisd.zesp.ambulanceservices.main.PatientState;
import aisd.zesp.ambulanceservices.main.ProgramAlgorithm;
import aisd.zesp.ambulanceservices.main.State;
import aisd.zesp.ambulanceservices.reading.Reader;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;

import static aisd.zesp.ambulanceservices.main.PatientState.*;
import static aisd.zesp.ambulanceservices.main.PatientState.ACCEPTED;


public class ScreenView extends GridPane {

    private final Stage primaryStage;
    private final ProgramAlgorithm programAlgorithm;
    private final Reader reader = new Reader();
    private State state;

    private Timeline timeline;

    private MapCanvas canvas;
    private HBox transportButtonsHBox;
    private HospitalVBox hospitalVBox;
    private PatientVBox patientVBox;
    private PatientTableView patientTableView;
    private HospitalTableView hospitalTableView;
    private InformationVBox informationVBox;
    private Button start;

    private final Color informationVBoxBackground = Color.color(79 / 255., 79 / 255., 79 / 255.);
    private final Color Background = Color.color(40 / 255., 40 / 255., 40 / 255.);
    private final Color patientAndHospitalVBoxesBackground = Color.color(24 / 255., 24 / 255., 24 / 255.);
    private final Color buttonBackground = Color.color(46 / 255., 46 / 255., 46 / 255.);

    private final Color ridingBackground = Color.color(29 / 255., 107 / 255., 150 / 255.);
    private final Color waitingBackground = Color.color(79 / 255., 79 / 255., 79 / 255.);
    private final Color rejectedBackground = Color.color(93 / 255., 60 / 255., 124 / 255.);
    private final Color acceptedBackground = Color.color(48 / 255., 136 / 255., 60 / 255.);
    private final Color abandonedBackground = Color.color(14 / 255., 15 / 255., 15 / 255.);

    public ScreenView(Stage primaryStage, ProgramAlgorithm programAlgorithm) {
        this.primaryStage = primaryStage;
        this.programAlgorithm = programAlgorithm;
    }

    public void updateInfoBox() {
        PatientState patientState = programAlgorithm.getCurrentPatient().getPatientState();
        Color bgColor = waitingBackground;

        if (patientState == RIDING) {
            bgColor = ridingBackground;
        } else if (patientState == OUTOFBOUNDS || patientState == ABANDONED) {
            bgColor = abandonedBackground;
        } else if (patientState == REJECTED) {
            bgColor = rejectedBackground;
        } else if (patientState == ACCEPTED) {
            bgColor = acceptedBackground;
        }

        transportButtonsHBox.setBackground(new Background(new BackgroundFill(bgColor, CornerRadii.EMPTY, Insets.EMPTY)));

        informationVBox.showInformation(bgColor);
    }

    public void draw() {

        this.timeline = new Timeline(new KeyFrame(Duration.millis(1500), this::handleNextStep));
        this.timeline.setCycleCount(Timeline.INDEFINITE);


        hospitalVBox = new HospitalVBox(programAlgorithm);
        patientVBox = new PatientVBox(programAlgorithm);
        informationVBox = new InformationVBox(programAlgorithm);
        patientTableView = new PatientTableView(programAlgorithm);
        hospitalTableView = new HospitalTableView(programAlgorithm);


        HBox root = new HBox(0);
        root.setPrefHeight(800);
        root.setAlignment(Pos.CENTER);


        start = new Button("");
        start.setOnAction(this::handleStart);
        ImageView viewPlay = new ImageView(AppAssets.play);
        start.setGraphic(viewPlay);
        start.setPrefSize(40, 40);

        Button next = new Button("");
        next.setOnAction(this::handleNextStep);
        ImageView view = new ImageView(AppAssets.step);
        next.setGraphic(view);
        next.setPrefSize(40, 40);


        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File("data"));
        fileChooser.setTitle("Wybierz plik z rozszerzeniem *.txt");


        Button loadMap = new Button("Otwórz mapę...");

        loadMap.setOnAction(
                e -> {
                    if (programAlgorithm.getState() != null) {
                        Alerts.showAlert("Mapa jest już załadowana");
                        return;
                    }

                    fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("txt Files", "*.txt"));
                    File file = fileChooser.showOpenDialog(primaryStage);

                    try {
                        state = reader.load(file.getAbsolutePath());
                        state.finalizeConnections();
                        state.finalizeConvexHull();
                        programAlgorithm.setState(state);
                        canvas.autoAlignView();
                        canvas.draw();
                        hospitalTableView.refreshHospitalslist();
                        // hospitalVBox.showHospital();
                    } catch (IllegalArgumentException ex) {
                        Alerts.showAlert(ex.getMessage());
                    }
                }
        );

        Button loadPatientsList = new Button("Dodaj pacjentów...");
        loadPatientsList.setOnAction(
                e -> {
                    if (programAlgorithm.getState() == null) {
                        Alerts.showAlert("Najpierw należy załadowac mapę państwa");

                        return;
                    }

                    fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("txt Files", "*.txt"));
                    File file = fileChooser.showOpenDialog(primaryStage);

                    String errorMessage = null;
                    try {
                        reader.loadPatients(programAlgorithm.getState(), file.getAbsolutePath());
                        canvas.draw();
                        patientTableView.refreshPatientslist();
                        // patientVBox.showPatient();
                    } catch (IllegalArgumentException ex) {
                        errorMessage = ex.getMessage();
                        Alerts.showAlert(errorMessage);
                    }
                }
        );


        HBox hbox = new HBox(4);
        hbox.setAlignment(Pos.CENTER_LEFT);
        hbox.setPadding(new Insets(4, 12, 4, 12));

        hbox.getChildren().addAll(loadMap, loadPatientsList);


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
        vbox.setPrefWidth(850);
        VBox.setVgrow(vbox, Priority.ALWAYS);

        canvas = new MapCanvas(800, 762, programAlgorithm);
        vbox.getChildren().add(canvas);

        canvas.setOnMouseClicked(eventHandler);


        VBox vbox2 = new VBox(0);
        vbox2.setAlignment(Pos.CENTER_RIGHT);

        vbox2.setBackground(new Background(new BackgroundFill(patientAndHospitalVBoxesBackground, CornerRadii.EMPTY, Insets.EMPTY)));


        VBox vbox3 = new VBox(0);

        vbox3.setBackground(new Background(new BackgroundFill(patientAndHospitalVBoxesBackground, CornerRadii.EMPTY, Insets.EMPTY)));
        vbox3.setPrefHeight(300);
        vbox3.setStyle("-fx-border-style: solid;" + "-fx-border-width: 1;" + "-fx-border-color: black");

        informationVBox.setBackground(new Background(new BackgroundFill(informationVBoxBackground, CornerRadii.EMPTY, Insets.EMPTY)));
        informationVBox.setMaxHeight(270);

        transportButtonsHBox = new HBox(4);
        transportButtonsHBox.setPadding(new Insets(4, 12, 4, 12));
        transportButtonsHBox.setBackground(new Background(new BackgroundFill(waitingBackground, CornerRadii.EMPTY, Insets.EMPTY)));
        transportButtonsHBox.setAlignment(Pos.TOP_LEFT);

        transportButtonsHBox.getChildren().addAll(start, next);
        vbox3.getChildren().addAll(transportButtonsHBox, informationVBox);

        informationVBox.setPrefHeight(300);
        informationVBox.setStyle("-fx-border-style: solid;"
                + "-fx-border-width: 0;"
                + "-fx-border-color: black");


        patientTableView.setBackground(new Background(new BackgroundFill(patientAndHospitalVBoxesBackground,
                CornerRadii.EMPTY,
                Insets.EMPTY)));
        patientTableView.setPrefHeight(300);
        patientTableView.setStyle("-fx-border-style: solid;"
                + "-fx-border-width: 0.5;"
                + "-fx-border-color: black");

        hospitalTableView.setBackground(new Background(new BackgroundFill(patientAndHospitalVBoxesBackground,
                CornerRadii.EMPTY,
                Insets.EMPTY)));
        hospitalTableView.setPrefHeight(300);
        hospitalTableView.setStyle("-fx-border-style: solid;"
                + "-fx-border-width: 0.5;"
                + "-fx-border-color: black");


        VBox.setVgrow(informationVBox, Priority.ALWAYS);
        VBox.setVgrow(hospitalTableView, Priority.ALWAYS);
        VBox.setVgrow(patientTableView, Priority.ALWAYS);


        vbox2.getChildren().addAll(vbox3, hospitalTableView, patientTableView);

        root.getChildren().addAll(vbox, vbox2);
        Scene scene = new Scene(root, 1300, 800);

        this.add(root, 0, 0);


        patientTableView.showPatient();
        hospitalTableView.showHospital();
        primaryStage.setTitle("Ambulance services");
        primaryStage.setScene(scene);
        primaryStage.show();

        canvas.draw();
    }


    private void handleStop(ActionEvent actionEvent) {
        this.timeline.stop();
        start.setOnAction(this::handleStart);

        ImageView viewStart = new ImageView(AppAssets.play);
        start.setGraphic(viewStart);
    }

    private void handleStart(ActionEvent actionEvent) {

        ImageView viewStop = new ImageView(AppAssets.pause);
        start.setGraphic(viewStop);
        this.timeline.play();
        start.setOnAction(this::handleStop);

        handleNextStep(actionEvent);
    }


    private void handleNextStep(ActionEvent actionEvent) {

        int flag = programAlgorithm.nextStep();
        if (flag == 1) {
            start.setOnAction(this::handleStop);
        }
        canvas.draw();
        updateInfoBox();
        patientTableView.refreshPatientslist();
        hospitalTableView.refreshHospitalslist();


    }

    EventHandler<MouseEvent> eventHandler = new EventHandler<>() {
        @Override
        public void handle(MouseEvent e) {
            if (programAlgorithm.getState() != null && e.getButton() == MouseButton.PRIMARY) {
                GraphicsContext g = canvas.getGraphicsContext2D();
                Point canvasCoords = new Point(e.getSceneX(), e.getSceneY() - canvas.getLayoutY());
                Point worldCoords = canvas.canvasToWorld(canvasCoords);

                state.addPatientFromCanvas(worldCoords.getX(), worldCoords.getY());

                patientTableView.refreshPatientslist();
                canvas.draw();
            }
        }
    };
}
