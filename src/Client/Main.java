package Client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        // Load the FXML file
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));

        // Set the title of the stage
        primaryStage.setTitle("Messenger!");

        // Create a new scene with the loaded FXML file and set its dimensions
        primaryStage.setScene(new Scene(root, 330, 560));

        // Disable the window resize option
        primaryStage.setResizable(false);

        // Display the stage
        primaryStage.show();
    }

    public static void main(String[] args) {
        // Launch the JavaFX application
        launch(args);
    }
}
