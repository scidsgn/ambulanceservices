package aisd.zesp.ambulanceservices.screen;

import aisd.zesp.ambulanceservices.geometry.Point;
import aisd.zesp.ambulanceservices.main.Patient;
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
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;


public class ScreenView extends GridPane {

    private final Stage primaryStage;
    private final ProgramAlgorithm programAlgorithm;
    private final Reader reader = new Reader();
    private State state;

    private Timeline timeline;

    private MapCanvas canvas;
    private HospitalVBox hospitalVBox;
    private PatientVBox patientVBox;
    private InformationVBox informationVBox;
    private Button start;

    public ScreenView(Stage primaryStage, ProgramAlgorithm programAlgorithm) {
        this.primaryStage = primaryStage;
        this.programAlgorithm = programAlgorithm;
    }


    public void draw() {

        this.timeline = new Timeline(new KeyFrame(Duration.millis(1500), this::handleNextStep));
        this.timeline.setCycleCount(Timeline.INDEFINITE);


        hospitalVBox = new HospitalVBox(programAlgorithm);
        patientVBox = new PatientVBox(programAlgorithm);
        informationVBox = new InformationVBox(programAlgorithm);


        HBox root = new HBox(0);
        root.setPrefHeight(800);
        root.setAlignment(Pos.CENTER);


        start = new Button("");
        start.setOnAction(this::handleStart);
        ImageView viewPlay = new ImageView(AppIcons.play);
        start.setGraphic(viewPlay);
        start.setPrefSize(30, 30);
        start.setTranslateX(5);

        Button next = new Button("");
        next.setOnAction(this::handleNextStep);
        ImageView view = new ImageView(AppIcons.hospital);
        next.setGraphic(view);
        next.setPrefSize(30, 30);


        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File("data"));
        fileChooser.setTitle("Wybierz plik z rozszerzeniem *.txt");


        Button loadMap = new Button("Wczytaj mapę");
        loadMap.setFont(Font.font("verdana", FontWeight.BLACK, FontPosture.REGULAR, 12));

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
                        Alerts.showAlert("Najpierw należy załadowac mapę państwa");

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


        loadMap.setTranslateX(5);


        HBox hbox = new HBox(10);
        hbox.setAlignment(Pos.CENTER_LEFT);

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
        vbox.setPrefWidth(1000);
        VBox.setVgrow(vbox, Priority.ALWAYS);

        canvas = new MapCanvas(999, 755, programAlgorithm);
        vbox.getChildren().add(canvas);

        canvas.setOnMouseClicked(eventHandler);


        VBox vbox2 = new VBox(0);
        vbox2.setAlignment(Pos.CENTER_RIGHT);


        VBox vbox3 = new VBox(20);


        vbox3.setBackground(new Background(new BackgroundFill(Color.DARKGRAY, CornerRadii.EMPTY, Insets.EMPTY)));
        vbox3.setPrefWidth(620);
        vbox3.setPrefHeight(300);
        vbox3.setStyle("-fx-border-style: solid;" + "-fx-border-width: 1;" + "-fx-border-color: black");

        HBox hbox2 = new HBox(10);
        hbox2.setAlignment(Pos.TOP_LEFT);

        hbox2.getChildren().addAll(start, next);
        vbox3.getChildren().addAll(hbox2, informationVBox);


        informationVBox.setBackground(new Background(new BackgroundFill(Color.DARKGRAY,
                CornerRadii.EMPTY,
                Insets.EMPTY)));
        informationVBox.setPrefWidth(620);
        informationVBox.setPrefHeight(300);
        informationVBox.setStyle("-fx-border-style: solid;"
                + "-fx-border-width: 0;"
                + "-fx-border-color: black");




        hospitalVBox.setBackground(new Background(new BackgroundFill(Color.DIMGRAY,
                CornerRadii.EMPTY,
                Insets.EMPTY)));
        hospitalVBox.setPrefWidth(620);
        hospitalVBox.setPrefHeight(300);
        hospitalVBox.setStyle("-fx-border-style: solid;"
                + "-fx-border-width: 0.5;"
                + "-fx-border-color: black");


        patientVBox.setBackground(new Background(new BackgroundFill(Color.DIMGRAY,
                CornerRadii.EMPTY,
                Insets.EMPTY)));
        patientVBox.setPrefWidth(620);
        patientVBox.setPrefHeight(300);
        patientVBox.setStyle("-fx-border-style: solid;"
                + "-fx-border-width: 0.5;"
                + "-fx-border-color: black");




        Text tx2 = new Text(" Szpitale");
        tx2.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.ITALIC, 12));

        Text tx3 = new Text(" Pacjenci");
        tx3.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.ITALIC, 12));

        VBox.setVgrow(informationVBox, Priority.ALWAYS);
        VBox.setVgrow(hospitalVBox, Priority.ALWAYS);
        VBox.setVgrow(patientVBox, Priority.ALWAYS);


        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
        hospitalVBox.getChildren().addAll(tx2);
        scrollPane.setContent(hospitalVBox);

        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);


        ScrollPane scrollPane2 = new ScrollPane();
        patientVBox.getChildren().addAll(tx3);
        scrollPane2.setContent(patientVBox);
        scrollPane2.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));

        scrollPane2.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        scrollPane2.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);


        vbox2.getChildren().addAll(vbox3, scrollPane, scrollPane2);

        root.getChildren().addAll(vbox, vbox2);
        Scene scene = new Scene(root, 1540, 900);

        this.add(root, 0, 0);


        primaryStage.setTitle("Ambulance services");
        primaryStage.setScene(scene);
        primaryStage.show();

        canvas.draw();
    }


    private void handleStop(ActionEvent actionEvent) {
        this.timeline.stop();
        start.setOnAction(this::handleStart);
        ImageView viewStart = new ImageView(AppIcons.play);
        start.setGraphic(viewStart);
    }

    private void handleStart(ActionEvent actionEvent) {

        ImageView viewStop = new ImageView(AppIcons.pause);
        start.setGraphic(viewStop);
        this.timeline.play();
        start.setOnAction(this::handleStop);

    }


    private void handleNextStep(ActionEvent actionEvent) {

        programAlgorithm.nextStep();
        if (programAlgorithm.nextStep() == 1) {
            start.setOnAction(this::handleStop);
        }
        canvas.draw();
        hospitalVBox.showHospital();
        patientVBox.showPatient();
        informationVBox.showInformation();


    }

    EventHandler<MouseEvent> eventHandler = new EventHandler<>() {
        @Override
        public void handle(MouseEvent e) {
            if (programAlgorithm.getState() != null && e.getButton() == MouseButton.PRIMARY) {
                GraphicsContext g = canvas.getGraphicsContext2D();
                Point canvasCoords = new Point(e.getSceneX(), e.getSceneY() - canvas.getLayoutY());
                Point worldCoords = canvas.canvasToWorld(canvasCoords);

                state.addPatientFromCanvas(worldCoords.getX(), worldCoords.getY());

                patientVBox.showPatient();
                canvas.draw();
            }
        }
    };
}
