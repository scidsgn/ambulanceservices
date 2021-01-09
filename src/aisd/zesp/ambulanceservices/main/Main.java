package aisd.zesp.ambulanceservices.main;


import aisd.zesp.ambulanceservices.screen.ScreenView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception{
        ScreenView screenView = new ScreenView(stage);
        screenView.draw();
        Scene scene = new Scene(screenView);
        stage.setScene(scene);
        stage.show();

    }


    public static void main(String[] args) {
        launch(args);
    }
}
