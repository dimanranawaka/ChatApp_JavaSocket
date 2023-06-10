package Client;

import Client.Controller.*;
import animatefx.animation.FadeIn;
import animatefx.animation.FadeOut;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.NodeOrientation;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.commons.lang3.StringUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

import static Client.Controller.loggedInUser;
import static Client.Controller.users;

public class Room extends Thread implements Initializable {
    // JavaFX controls
    @FXML
    public Label clientName;
    @FXML
    public Button chatBtn;
    @FXML
    public Pane chat;
    @FXML
    public TextField msgField;
    @FXML
    public TextArea msgRoom;
    @FXML
    public Label online;
    @FXML
    public Label fullName;
    @FXML
    public Label email;
    @FXML
    public Label phoneNo;
    @FXML
    public Label gender;
    @FXML
    public Pane profile;
    @FXML
    public Button profileBtn;
    @FXML
    public TextField fileChoosePath;
    @FXML
    public ImageView proImage;
    @FXML
    public Circle showProPic;
    public ImageView imgPhoto;
    public ImageView emoji;

    // FileChooser and File path
    private FileChooser fileChooser;
    private File filePath;

    // Chat and profile toggles
    public boolean toggleChat = false, toggleProfile = false;

    // Socket communication
    BufferedReader reader;
    PrintWriter writer;
    Socket socket;

    // Connect to the server socket
    public void connectSocket() {
        try {
            socket = new Socket("localhost", 8889);
            System.out.println("Socket is connected with server!");
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(socket.getOutputStream(), true);
            this.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            while (true) {
                // Read messages from the server
                String msg = reader.readLine(); // Read a line of text from the server
                String[] tokens = msg.split(" "); // Split the received message into an array of strings
                String cmd = tokens[0]; // Extract the first token, which represents a command or sender information
                System.out.println(cmd); // Print the command to the console for debugging
                StringBuilder fulmsg = new StringBuilder();
                for(int i = 1; i < tokens.length; i++) {
                    fulmsg.append(tokens[i]); // Reconstruct the complete message (excluding the command)
                }
                System.out.println(fulmsg); // Print the reconstructed message to the console for debugging
                if (cmd.equalsIgnoreCase(Controller.username + ":")) {
                    continue;   // Skip to the next iteration if the command matches the current user's username
                } else if(fulmsg.toString().equalsIgnoreCase("bye")) {
                    break;  // Break the loop if the reconstructed message equals "bye" (ignoring case)
                }
                // Display received messages in the message room
                msgRoom.appendText(msg + "\n"); // Append the received message to the message room
            }
            reader.close(); // Close the reader
            writer.close(); // Close the writer
            socket.close(); // Close the socket
        } catch (Exception e) {
            e.printStackTrace(); // Print the stack trace of any exceptions that occur
        }
    }

    // Handle the profile button click event
    public void handleProfileBtn(ActionEvent event) {
        if (event.getSource().equals(profileBtn) && !toggleProfile) {
            // Show profile pane
            new FadeIn(profile).play();
            profile.toFront();
            chat.toBack();
            toggleProfile = true;
            toggleChat = false;
            profileBtn.setText("Back");
            setProfile();
        } else if (event.getSource().equals(profileBtn) && toggleProfile) {
            // Show chat pane
            new FadeIn(chat).play();
            chat.toFront();
            toggleProfile = false;
            toggleChat = false;
            profileBtn.setText("Profile");
        }
    }

    // Set the user profile information
    public void setProfile() {
        for (User user : users) {
            if (Controller.username.equalsIgnoreCase(user.name)) {
                fullName.setText(user.fullName);
                fullName.setOpacity(1);
                email.setText(user.email);
                email.setOpacity(1);
                phoneNo.setText(user.phoneNo);
                gender.setText(user.gender);
            }
        }
    }

    // Handle the send button click event
    public void handleSendEvent(MouseEvent event) {
        send();
        for(User user : users) {
            System.out.println(user.name);
        }
    }

    // Send a message to the server
    public void send() {
        String msg = msgField.getText();
        writer.println(Controller.username + ": " + msg);
        msgRoom.setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
        msgRoom.appendText("Me: " + msg + "\n");
        msgField.setText("");
        if(msg.equalsIgnoreCase("BYE") || (msg.equalsIgnoreCase("logout"))) {
            System.exit(0);
        }
    }

    // Choose an image for the profile picture
    public boolean saveControl = false;
    public void chooseImageButton(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        fileChooser = new FileChooser();
        fileChooser.setTitle("Open Image");
        this.filePath = fileChooser.showOpenDialog(stage);
        fileChoosePath.setText(filePath.getPath());
        saveControl = true;
    }

    // Send a message when Enter key is pressed
    public void sendMessageByKey(KeyEvent event) {
        if (event.getCode().toString().equals("ENTER")) {
            send();
        }
    }

    // Save the chosen image as the profile picture
    public void saveImage() {
        if (saveControl) {
            try {
                BufferedImage bufferedImage = ImageIO.read(filePath);
                Image image = SwingFXUtils.toFXImage(bufferedImage, null);
                proImage.setImage(image);
                showProPic.setFill(new ImagePattern(image));
                saveControl = false;
                fileChoosePath.setText("");
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }
        }
    }

    //Send Image to the Server
    public void imgCamaraOnAction(MouseEvent mouseEvent) {
        try {
            // Get the reference to the current stage (window)
            Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();

            // Create a FileChooser dialog to choose an image file
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open Image");

            // Show the FileChooser dialog and wait for the user to select an image file
            this.filePath = fileChooser.showOpenDialog(stage);

            // Send the image file path to the server as a message
            // The message format is "<clientName> img <filePath>"
            writer.println(clientName.getText() + " " + "img" + filePath.getPath());
            writer.flush();
        } catch (NullPointerException e) {
            // Handle the case when no image is selected
            System.out.println("Image is not selected!");
        }
    }

    //Send emojis to the Server
    public void sendEmoji(MouseEvent mouseEvent) {
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        showProPic.setStroke(Color.valueOf("#90a4ae"));
        Image image;
        if(Controller.gender.equalsIgnoreCase("Male")) {
            image = new Image("icons/user.png", false);
        } else {
            image = new Image("icons/female.png", false);
            proImage.setImage(image);
        }
        showProPic.setFill(new ImagePattern(image));
        clientName.setText(Controller.username);
        connectSocket();
    }


}
