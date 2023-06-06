package com.PlayTechPvtLtd.LiveChatApplication.controller;

import animatefx.animation.FadeIn;
import com.PlayTechPvtLtd.LiveChatApplication.model.User;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.geometry.NodeOrientation;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;

import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

import static com.PlayTechPvtLtd.LiveChatApplication.controller.CreateNewUserAccountController.users;

public class InboxManagerController extends Thread implements Initializable {
    public Pane profile;
    public Label fullName;
    public Label email;
    public Label phoneNo;
    public Label gender;
    public TextField fileChoosePath;
    public ImageView proImage;
    public TextArea msgRoom;
    public Pane chat;
    public TextField msgField;
    public ImageView emojiBtn;
    public ImageView imageBtn;
    public Label clientName;
    public Button profileBtn;
    public Circle showProPic;
    public FileChooser fileChooser;
    private File filePath;
    public boolean toggleChat = false , toggleProfile = false;

    BufferedReader reader;
    PrintWriter writer;
    Socket socket;

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

    public void handleProfileBtn(ActionEvent actionEvent) {
        if(actionEvent.getSource().equals(profileBtn) && !toggleProfile){
            new FadeIn(profile).play();
            profile.toFront();
            chat.toBack();
            toggleProfile = true;
            toggleChat = false;
            profileBtn.setText("Back");
            setProfile();
        }else if (actionEvent.getSource().equals(profileBtn) && toggleProfile) {
            new FadeIn(chat).play();
            chat.toFront();
            toggleProfile = false;
            toggleChat = false;
            profileBtn.setText("Profile");
        }
    }

    public void setProfile() {
        for (User user :users ) {
            if (CreateNewUserAccountController.username.equalsIgnoreCase(user.name)) {
                fullName.setText(user.fullName);
                fullName.setOpacity(1);
                email.setText(user.email);
                email.setOpacity(1);
                phoneNo.setText(user.phoneNo);
                gender.setText(user.gender);
            }
        }
    }
    public void imageBtnOnAction(MouseEvent mouseEvent) {
    }

    public void emojiBtnOnAction(MouseEvent mouseEvent) {
    }

    public void sendMessageByKey(KeyEvent keyEvent) {
    }

    public void handleSendEvent(MouseEvent mouseEvent) {
        send();
        for(User user : users) {
            System.out.println(user.name);
        }
    }
    public void send() {
        String msg = msgField.getText();
        writer.println(CreateNewUserAccountController.username + ": " + msg);
        msgRoom.setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
        msgRoom.appendText("Me: " + msg + "\n");
        msgField.setText("");
        if(msg.equalsIgnoreCase("BYE") || (msg.equalsIgnoreCase("logout"))) {
            System.exit(0);
        }
    }
    public void saveImage(ActionEvent actionEvent) {
    }

    public void chooseImageButton(ActionEvent actionEvent) {
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        showProPic.setStroke(Color.valueOf("#90a4ae"));
        Image image;
        if(CreateNewUserAccountController.gender.equalsIgnoreCase("Male")) {
            image = new Image("/com/PlayTechPvtLtd/LiveChatApplication/view/assets/icons/user.png", false);
        } else {
            image = new Image("/com/PlayTechPvtLtd/LiveChatApplication/view/assets/icons/female.png", false);
            proImage.setImage(image);
        }
        showProPic.setFill(new ImagePattern(image));
        clientName.setText(CreateNewUserAccountController.username);
        connectSocket();
    }
}
