package LMS;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;

import java.util.Optional;

public class HandleAlertOperations {
    /**
     * Displays an information alert with the specified title and content.
     *
     * @param title   the title of the alert dialog
     * @param content the content text of the alert dialog
     */
    public static void showAlert(String title, String content) {
        Platform.runLater(() -> { // Ensure this code runs on FX Application Thread
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(title);
            alert.setHeaderText(null);

            // Wrap text for content
            Label contentLabel = new Label(content);
            contentLabel.setWrapText(true);
            alert.getDialogPane().setContent(contentLabel);

            alert.showAndWait(); // Simply show the alert
        });

        try {
            // Wait for the alert to be processed on FX Application Thread
            Thread.sleep(500); // Adjust as necessary based on app responsiveness
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Restore interrupted state
        }
    }

    /**
     * Displays an error alert with the specified title and content.
     * @param title the title of the alert dialog
     * @param content the content text of the alert dialog
     *
     * @return true if the user clicks "OK", false otherwise
     */
    public static boolean showConfirmation(String title, String content) {
        final boolean[] result = {true}; // Default return value is true

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);

        // Wrap text for content
        Label contentLabel = new Label(content);
        contentLabel.setWrapText(true);
        alert.getDialogPane().setContent(contentLabel);

        ButtonType yesButton = new ButtonType("Yes", ButtonBar.ButtonData.YES);
        ButtonType noButton = new ButtonType("No", ButtonBar.ButtonData.NO);
        alert.getButtonTypes().setAll(yesButton, noButton);

        Optional<ButtonType> choice = alert.showAndWait();
        result[0] = choice.isPresent() && choice.get() == yesButton; // True if "Yes" is selected, false otherwise

        return result[0];
    }
}
