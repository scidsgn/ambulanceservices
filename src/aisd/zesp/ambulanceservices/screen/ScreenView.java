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
    private PatientTableView patientTableView;
    private HospitalTableView hospitalTableView;
    private InformationVBox informationVBox;
    private Button start;

    private final Color informationVBoxBackground = Color.color(79/255., 79/255., 79/255.);
    private final Color Background = Color.color(40/255., 40/255., 40/255.);
    private final Color patientAndHospitalVBoxesBackground = Color.color(24/255., 24/255., 24/255.);
    private final Color buttonBackground = Color.color(46/255., 46/255., 46/255.);

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
        patientTableView = new PatientTableView(programAlgorithm);
        hospitalTableView = new HospitalTableView(programAlgorithm);


        HBox root = new HBox(0);
        root.setPrefHeight(800);
        root.setAlignment(Pos.CENTER);


        start = new Button("");
        start.setOnAction(this::handleStart);
        ImageView viewPlay = new ImageView(AppIcons.play);
        start.setGraphic(viewPlay);
        start.setPrefSize(40, 40);
        start.setBackground(new Background(new BackgroundFill(buttonBackground,
                CornerRadii.EMPTY,
                Insets.EMPTY)));
        start.setTranslateX(5);

        Button next = new Button("");
        next.setOnAction(this::handleNextStep);
        ImageView view = new ImageView(AppIcons.step);
        next.setGraphic(view);
        next.setPrefSize(40, 40);
        next.setBackground(new Background(new BackgroundFill(buttonBackground,
                CornerRadii.EMPTY,
                Insets.EMPTY)));


        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File("data"));
        fileChooser.setTitle("Wybierz plik z rozszerzeniem *.txt");


        Button loadMap = new Button("Wczytaj mapę");
        loadMap.setFont(Font.font("verdana", FontWeight.BLACK, FontPosture.REGULAR, 12));
        loadMap.setBackground(new Background(new BackgroundFill(buttonBackground,
                CornerRadii.EMPTY,
                Insets.EMPTY)));

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
                        hospitalTableView.refreshHospitalslist();
                       // hospitalVBox.showHospital();
                    } catch (IllegalArgumentException ex) {
                        Alerts.showAlert(ex.getMessage());
                    }
                }
        );

        Button loadPatientsList = new Button("Wczytaj listę pacjentów");
        loadPatientsList.setFont(Font.font("verdana", FontWeight.BLACK, FontPosture.REGULAR, 12));
        loadPatientsList.setBackground(new Background(new BackgroundFill(buttonBackground,
                CornerRadii.EMPTY,
                Insets.EMPTY)));
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
                        patientTableView.refreshPatientslist();;
                       // patientVBox.showPatient();
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

        vbox2.setBackground(new Background(new BackgroundFill(patientAndHospitalVBoxesBackground, CornerRadii.EMPTY, Insets.EMPTY)));


        VBox vbox3 = new VBox(0);

        vbox3.setBackground(new Background(new BackgroundFill(patientAndHospitalVBoxesBackground, CornerRadii.EMPTY, Insets.EMPTY)));
        vbox3.setPrefWidth(620);
        vbox3.setPrefHeight(300);
        vbox3.setStyle("-fx-border-style: solid;" + "-fx-border-width: 1;" + "-fx-border-color: black");

        informationVBox.setBackground(new Background(new BackgroundFill(informationVBoxBackground, CornerRadii.EMPTY, Insets.EMPTY)));
        informationVBox.setMaxHeight(270);

        HBox hbox2 = new HBox(10);
        hbox2.setBackground(new Background(new BackgroundFill(patientAndHospitalVBoxesBackground, CornerRadii.EMPTY, Insets.EMPTY)));
        hbox2.setAlignment(Pos.TOP_LEFT);

        hbox2.getChildren().addAll(start, next);
        vbox3.getChildren().addAll(hbox2, informationVBox);



        informationVBox.setPrefWidth(620);
        informationVBox.setPrefHeight(300);
        informationVBox.setStyle("-fx-border-style: solid;"
                + "-fx-border-width: 0;"
                + "-fx-border-color: black");




        patientTableView.setBackground(new Background(new BackgroundFill(patientAndHospitalVBoxesBackground,
                CornerRadii.EMPTY,
                Insets.EMPTY)));
        patientTableView.setPrefWidth(300);
        patientTableView.setPrefHeight(300);
        patientTableView.setStyle("-fx-border-style: solid;"
                + "-fx-border-width: 0.5;"
                + "-fx-border-color: black");

        hospitalTableView.setBackground(new Background(new BackgroundFill(patientAndHospitalVBoxesBackground,
                CornerRadii.EMPTY,
                Insets.EMPTY)));
        hospitalTableView.setPrefWidth(200);
        hospitalTableView.setPrefHeight(300);
        hospitalTableView.setStyle("-fx-border-style: solid;"
                + "-fx-border-width: 0.5;"
                + "-fx-border-color: black");




        VBox.setVgrow(informationVBox, Priority.ALWAYS);
        VBox.setVgrow(hospitalTableView, Priority.ALWAYS);
        VBox.setVgrow( patientTableView, Priority.ALWAYS);



        vbox2.getChildren().addAll(vbox3, hospitalTableView, patientTableView);

        root.getChildren().addAll(vbox, vbox2);
        Scene scene = new Scene(root, 1540, 900);

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

        int flag =  programAlgorithm.nextStep();
        if (flag == 1) {
            start.setOnAction(this::handleStop);
        }
        canvas.draw();
        informationVBox.showInformation();
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
