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
    private PatientTableView patientTableView;
    private HospitalTableView hospitalTableView;
    private InformationVBox informationVBox;
    private Button startButton, nextButton;

    private final Color informationVBoxBackground = Color.color(79 / 255., 79 / 255., 79 / 255.);
    private final Color patientAndHospitalVBoxesBackground = Color.color(24 / 255., 24 / 255., 24 / 255.);
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

    public void showInfoMessage(String errorMessage) {
        transportButtonsHBox.setBackground(new Background(new BackgroundFill(informationVBoxBackground, CornerRadii.EMPTY, Insets.EMPTY)));
        informationVBox.showInfoMessage(errorMessage);
    }

    public void showErrorMessage(String message) {
        transportButtonsHBox.setBackground(new Background(new BackgroundFill(Color.DARKRED, CornerRadii.EMPTY, Insets.EMPTY)));
        informationVBox.showErrorMessage(message);
    }

    public void draw() {
        this.timeline = new Timeline(new KeyFrame(Duration.millis(1500), this::handleNextStep));
        this.timeline.setCycleCount(Timeline.INDEFINITE);

        informationVBox = new InformationVBox(programAlgorithm);
        patientTableView = new PatientTableView(programAlgorithm);
        hospitalTableView = new HospitalTableView(programAlgorithm);

        HBox root = new HBox(0);
        root.setPrefHeight(800);
        root.setAlignment(Pos.CENTER);

        startButton = new Button("");
        startButton.setOnAction(this::handleStart);
        ImageView viewPlay = new ImageView(AppAssets.play);
        startButton.setGraphic(viewPlay);
        startButton.setPrefSize(40, 40);
        startButton.setDisable(true);

        nextButton = new Button("");
        nextButton.setOnAction(this::handleNextStep);
        ImageView view = new ImageView(AppAssets.step);
        nextButton.setGraphic(view);
        nextButton.setPrefSize(40, 40);
        nextButton.setDisable(true);

        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File("data"));
        fileChooser.setTitle("Wybierz plik z rozszerzeniem *.txt");

        Button loadMap = new Button("Otwórz mapę...");
        Button loadPatientsList = new Button("Dodaj pacjentów...");
        loadPatientsList.setDisable(true);

        loadMap.setOnAction(
                e -> {
                    fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("txt Files", "*.txt"));
                    File file = fileChooser.showOpenDialog(primaryStage);

                    try {
                        state = reader.load(file.getAbsolutePath());
                        state.finalizeConnections();
                        state.finalizeConvexHull();
                        programAlgorithm.setState(state);
                        canvas.autoAlignView();
                        canvas.draw();
                        hospitalTableView.refreshHospitalsList();

                        showInfoMessage("Przed rozpoczęciem dodaj pacjentów");
                        loadPatientsList.setDisable(false);
                        loadMap.setDisable(true);
                    } catch (Exception ex) {
                        showErrorMessage(ex.getMessage());
                    }
                }
        );

        loadPatientsList.setOnAction(
                e -> {
                    fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("txt Files", "*.txt"));
                    File file = fileChooser.showOpenDialog(primaryStage);

                    String errorMessage = null;
                    try {
                        reader.loadPatients(programAlgorithm.getState(), file.getAbsolutePath());
                        canvas.draw();
                        patientTableView.refreshPatientsList();

                        showInfoMessage("Czekanie na uruchomienie systemu");
                        startButton.setDisable(false);
                        nextButton.setDisable(false);
                    } catch (Exception ex) {
                        showErrorMessage(ex.getMessage());
                    }
                }
        );


        HBox leftTopHBox = new HBox(4);
        leftTopHBox.setAlignment(Pos.CENTER_LEFT);
        leftTopHBox.setPadding(new Insets(4, 12, 4, 12));
        leftTopHBox.getChildren().addAll(loadMap, loadPatientsList);


        VBox leftVBox = new VBox(0);
        leftVBox.setAlignment(Pos.BASELINE_LEFT);
        leftVBox.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
        leftVBox.getChildren().add(leftTopHBox);
        leftVBox.setPrefHeight(50);
        leftVBox.setPrefWidth(850);
        VBox.setVgrow(leftVBox, Priority.ALWAYS);

        canvas = new MapCanvas(800, 742, programAlgorithm);
        canvas.setOnMouseClicked(eventHandler);
        leftVBox.getChildren().add(canvas);

        VBox rightVBox = new VBox(0);
        rightVBox.setAlignment(Pos.CENTER_RIGHT);
        rightVBox.setBackground(new Background(
                new BackgroundFill(patientAndHospitalVBoxesBackground, CornerRadii.EMPTY, Insets.EMPTY)
        ));

        VBox rightTopVBox = new VBox(0);
        rightTopVBox.setBackground(new Background(new BackgroundFill(patientAndHospitalVBoxesBackground, CornerRadii.EMPTY, Insets.EMPTY)));
        rightTopVBox.setPrefHeight(300);

        informationVBox.setBackground(new Background(new BackgroundFill(informationVBoxBackground, CornerRadii.EMPTY, Insets.EMPTY)));
        informationVBox.setMaxHeight(270);
        informationVBox.setPrefHeight(300);

        transportButtonsHBox = new HBox(4);
        transportButtonsHBox.setPadding(new Insets(4, 12, 4, 12));
        transportButtonsHBox.setBackground(new Background(new BackgroundFill(waitingBackground, CornerRadii.EMPTY, Insets.EMPTY)));
        transportButtonsHBox.setAlignment(Pos.TOP_LEFT);
        transportButtonsHBox.getChildren().addAll(startButton, nextButton);

        rightTopVBox.getChildren().addAll(transportButtonsHBox, informationVBox);

        patientTableView.setBackground(new Background(new BackgroundFill(
                patientAndHospitalVBoxesBackground,
                CornerRadii.EMPTY,
                Insets.EMPTY
        )));
        patientTableView.setPrefHeight(300);
        patientTableView.setMaxHeight(479);

        hospitalTableView.setBackground(new Background(new BackgroundFill(
                patientAndHospitalVBoxesBackground,
                CornerRadii.EMPTY,
                Insets.EMPTY)
        ));
        hospitalTableView.setPrefHeight(300);
        hospitalTableView.setMaxHeight(479);

        VBox.setVgrow(informationVBox, Priority.ALWAYS);
        VBox.setVgrow(hospitalTableView, Priority.ALWAYS);
        VBox.setVgrow(patientTableView, Priority.ALWAYS);

        rightVBox.getChildren().addAll(rightTopVBox, hospitalTableView, patientTableView);

        root.getChildren().addAll(leftVBox, rightVBox);

        Scene scene = new Scene(root, 1300, 800);
        this.add(root, 0, 0);

        patientTableView.showPatient();
        hospitalTableView.showHospital();
        primaryStage.setTitle("Ambulance services");
        primaryStage.setScene(scene);
        primaryStage.show();

        canvas.draw();
        showInfoMessage("Przed rozpoczęciem otwórz plik mapy");
    }

    private void handleStop(ActionEvent actionEvent) {
        this.timeline.stop();
        startButton.setOnAction(this::handleStart);

        ImageView viewStart = new ImageView(AppAssets.play);
        startButton.setGraphic(viewStart);
    }

    private void handleStart(ActionEvent actionEvent) {
        ImageView viewStop = new ImageView(AppAssets.pause);
        startButton.setGraphic(viewStop);
        this.timeline.play();
        startButton.setOnAction(this::handleStop);

        handleNextStep(actionEvent);
    }

    private void handleNextStep(ActionEvent actionEvent) {
        if (programAlgorithm.nextStep() == 1) {
            startButton.setOnAction(this::handleStop);
        }

        canvas.draw();
        updateInfoBox();
        patientTableView.refreshPatientsList();
        hospitalTableView.refreshHospitalsList();
    }

    EventHandler<MouseEvent> eventHandler = new EventHandler<>() {
        @Override
        public void handle(MouseEvent e) {
            if (programAlgorithm.getState() != null && e.getButton() == MouseButton.PRIMARY) {
                GraphicsContext g = canvas.getGraphicsContext2D();
                Point canvasCoords = new Point(e.getSceneX(), e.getSceneY() - canvas.getLayoutY());
                Point worldCoords = canvas.canvasToWorld(canvasCoords);

                state.addPatientFromCanvas(worldCoords.getX(), worldCoords.getY());

                patientTableView.refreshPatientsList();
                canvas.draw();

                if (state.getPatientList().size() == 1) {
                    showInfoMessage("Czekanie na uruchomienie systemu");
                    startButton.setDisable(false);
                    nextButton.setDisable(false);
                }
            }
        }
    };
}
