package aisd.zesp.ambulanceservices.main;

import aisd.zesp.ambulanceservices.screen.ScreenView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;

public class Main extends Application {
    private final ProgramAlgorithm programAlgorithm = new ProgramAlgorithm();

    @Override
    public void start(Stage stage) {
        ScreenView screenView = new ScreenView(stage, programAlgorithm);
        screenView.draw();

        Scene scene = new Scene(screenView);
        scene.getStylesheets().add(new File("resources/stylesheet.css").toURI().toString());

        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
