import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ClientAppInitializer extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent load = FXMLLoader.load(getClass().getResource("com/PlayTechPvtLtd/LiveChatApplication/view/CreateNewAccount.fxml"));
        primaryStage.setTitle("DreedConnect");
        /*primaryStage.centerOnScreen();*/
        primaryStage.setScene(new Scene(load));
        primaryStage.setResizable(false);
        primaryStage.show();
    }
}
