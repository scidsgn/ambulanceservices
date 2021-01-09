package aisd.zesp.ambulanceservices.screen;

import javafx.scene.control.Alert;

public class Alerts {

    public static void showAlert(String errorText) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error message");
        alert.setHeaderText(null);
        alert.setContentText(errorText);
        alert.showAndWait();
    }

}
