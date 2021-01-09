package aisd.zesp.ambulanceservices.screen;

import aisd.zesp.ambulanceservices.main.*;
import aisd.zesp.ambulanceservices.reading.Reader;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;

import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;


public class ScreenView extends GridPane {

    private Stage primaryStage;
    private ProgramAlgorithm programAlgorithm;
    private final Reader reader = new Reader();

    public ScreenView(Stage primaryStage, ProgramAlgorithm programAlgorithm) {
        this.primaryStage = primaryStage;
        this.programAlgorithm = programAlgorithm;
    }

    public void draw() {
        HBox root = new HBox(0);
        root.setPadding(new Insets(10));
        root.setPrefHeight(800);
        root.setAlignment(Pos.CENTER);


        FileChooser fileChooser = new FileChooser();
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

                    State state = reader.load(file.getAbsolutePath());
                    state.finalizeConnections();
                    programAlgorithm.setState(state);
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

                    reader.loadPatients(programAlgorithm.getState(), file.getAbsolutePath());
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


        VBox vbox2 = new VBox(0);
        vbox2.setAlignment(Pos.CENTER_RIGHT);

        VBox vbox3 = new VBox(0);
        vbox3.setBackground(new Background(new BackgroundFill(Color.DARKGRAY,
                CornerRadii.EMPTY,
                Insets.EMPTY)));
        vbox3.setPrefWidth(620);
        vbox3.setMaxHeight(320);
        vbox3.setStyle("-fx-border-style: solid;"
                + "-fx-border-width: 1;"
                + "-fx-border-color: black");

        VBox vbox4 = new VBox(0);
        vbox4.setBackground(new Background(new BackgroundFill(Color.DIMGRAY,
                CornerRadii.EMPTY,
                Insets.EMPTY)));
        vbox4.setMaxWidth(620);
        vbox4.setMaxHeight(320);
        vbox4.setStyle("-fx-border-style: solid;"
                + "-fx-border-width: 1;"
                + "-fx-border-color: black");


        VBox vbox5 = new VBox(0);
        vbox5.setBackground(new Background(new BackgroundFill(Color.DIMGRAY,
                CornerRadii.EMPTY,
                Insets.EMPTY)));
        vbox5.setMaxWidth(620);
        vbox5.setMaxHeight(320);
        vbox5.setStyle("-fx-border-style: solid;"
                + "-fx-border-width: 1;"
                + "-fx-border-color: black");


        Button start = new Button("Start");
        start.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 12));

        start.setTranslateX(5);
        start.setTranslateY(3);
        Text tx1 = new Text("Symulacja tutaj ");
        tx1.setLayoutY(30);
        tx1.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.ITALIC, 20));


        Text tx2 = new Text(" Szpitale");
        tx2.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.ITALIC, 12));

        Text tx3 = new Text(" Pacjenci");
        tx3.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.ITALIC, 12));


        VBox.setVgrow(vbox3, Priority.ALWAYS);
        VBox.setVgrow(vbox4, Priority.ALWAYS);
        VBox.setVgrow(vbox5, Priority.ALWAYS);
        vbox3.getChildren().addAll(tx1);
        vbox4.getChildren().addAll(tx2);
        vbox5.getChildren().addAll(tx3);
        vbox2.getChildren().addAll(vbox3, vbox4, vbox5);


        root.getChildren().addAll(vbox, vbox2);
        Scene scene = new Scene(root, 1540, 900);

        this.add(root, 0, 0);


        primaryStage.setTitle("Ambulance services");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
