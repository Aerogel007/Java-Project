package JavaFX;

import javafx.util.Duration;

import javafx.animation.PauseTransition;
import javafx.scene.control.Button;
/*
 * this class houses two methods. which changed color of the button momentarily.
 * i have implemented this when the button is clicked. so we can notice clicking the button.
 */
public class ChangingButtonColor {
    // Reusable method to change color momentarily
    protected static void changeButtonColorTemporarilyForCopy(Button button) {
        // Change the button's color
        button.setStyle("-fx-background-color: #f8c06b; -fx-font-size: 7px;");
        FrontendProcess.staticLabel.setVisible(true); //shows copied! label


        // Create a PauseTransition to delay the revert
        PauseTransition pause = new PauseTransition();
        pause.setDuration(Duration.millis(200)); // 0.5 seconds delay (500 ms)
        pause.setOnFinished(event -> {
            // Revert the button color back after the delay
            button.setStyle("-fx-background-color: #e28d0d; -fx-font-size: 10px;");

        });

        // Start the transition
        pause.play();

        /*hides copied! when the pause1 plays */
        PauseTransition pause1 = new PauseTransition();
        pause1.setDuration(Duration.seconds(1));
        pause1.setOnFinished(event -> {
            FrontendProcess.staticLabel.setVisible(false);

        });
        pause1.play();
    }

    // Reusable method to change color momentarily
    protected static void changeButtonColorTemporarilyForButton(Button button) {
        // Change the button's color
        button.setStyle("-fx-background-color: #5b82cf; -fx-font-size: 10px; -fx");

        // Create a PauseTransition to delay the revert
        PauseTransition pause = new PauseTransition();
        pause.setDuration(Duration.millis(200)); // 0.5 seconds delay (500 ms)
        pause.setOnFinished(event -> {
            // Revert the button color back after the delay
            button.setStyle("-fx-background-color: #4668b3; -fx-font-size: 14px;");
        });

        // Start the transition
        pause.play();
    }
}
