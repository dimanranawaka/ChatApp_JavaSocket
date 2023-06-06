package com.PlayTechPvtLtd.LiveChatApplication.controller;

import animatefx.animation.FadeIn;
import com.PlayTechPvtLtd.LiveChatApplication.model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

public class CreateNewUserAccountController {
    public Pane pnSignUp;
    public ImageView btnBack;
    public PasswordField regPass;
    public TextField regFirstName;
    public TextField regEmail;
    public Button getStarted;
    public Label controlRegLabel;
    public Label success;
    public Label goBack;
    public TextField regName;
    public RadioButton male;
    public ToggleGroup Gender;
    public RadioButton female;
    public Label nameExists;
    public Label checkEmail;
    public TextField regPhoneNo;
    public Pane pnSignIn;
    public TextField userName;
    public PasswordField passWord;
    public Button btnSignUp;
    public Label loginNotifier;

    public static String username, password, gender;
    public static ArrayList<User> loggedInUser = new ArrayList<>();
    public static ArrayList<User> users = new ArrayList<User>();


     /*SignIn account  SignUp account to back button controller Code Start*/
    public void handleMouseEvent(MouseEvent mouseEvent) {
        if(mouseEvent.getSource() == btnBack){
            new FadeIn(pnSignIn).play();
            pnSignIn.toFront();
        }
        regEmail.setText("");
        regPass.setText("");
        regName.setText("");
    }
    /*SignIn account  SignUp account to back button controller Code Start*/

    /****************************/

    /*Registration Code Start*/
    public void registration(ActionEvent actionEvent) {
        if (!regName.getText().equalsIgnoreCase("")
                && !regPass.getText().equalsIgnoreCase("")
                && !regEmail.getText().equalsIgnoreCase("")
                && !regFirstName.getText().equalsIgnoreCase("")
                && !regPhoneNo.getText().equalsIgnoreCase("")
                && (male.isSelected() || female.isSelected())) {
            if(checkUser(regName.getText())) {
                if(checkEmail(regEmail.getText())) {
                    User newUser = new User();
                    newUser.name = regName.getText();
                    newUser.password = regPass.getText();
                    newUser.email = regEmail.getText();
                    newUser.fullName = regFirstName.getText();
                    newUser.phoneNo = regPhoneNo.getText();
                    if (male.isSelected()) {
                        newUser.gender = "Male";
                    } else {
                        newUser.gender = "Female";
                    }
                    users.add(newUser);
                    goBack.setOpacity(1);
                    success.setOpacity(1);
                    makeDefault();
                    if (controlRegLabel.getOpacity() == 1) {
                        controlRegLabel.setOpacity(0);
                    }
                    if (nameExists.getOpacity() == 1) {
                        nameExists.setOpacity(0);
                    }
                } else {
                    checkEmail.setOpacity(1);
                    setOpacity(nameExists, goBack, controlRegLabel, success);
                }
            } else {
                nameExists.setOpacity(1);
                setOpacity(success, goBack, controlRegLabel, checkEmail);
            }
        } else {
            controlRegLabel.setOpacity(1);
            setOpacity(success, goBack, nameExists, checkEmail);
        }
    }
    /*Registration Code End*/

    /*Switching Anchor Panes code Start*/
    public void handleButtonAction(ActionEvent actionEvent) {
        if(actionEvent.getSource().equals(btnSignUp)){
            new FadeIn(pnSignUp).play();
            pnSignUp.toFront();
        }
        if (actionEvent.getSource().equals(getStarted)){
            new FadeIn(pnSignIn).play();
            pnSignIn.toFront();
        }
        loginNotifier.setOpacity(0);
        userName.setText("");
        passWord.setText("");
    }
    /*Switching Anchor Panes code End*/

    /****************************/

    /*User Login Controller Code Start*/
    public void login(ActionEvent actionEvent) {
        username = userName.getText();
        password = passWord.getText();
        boolean login = false;
        for (User x : users) {
            if (x.name.equalsIgnoreCase(username) && x.password.equalsIgnoreCase(password)) {
                login = true;
                loggedInUser.add(x);
                System.out.println(x.name);
                gender = x.gender;
                break;
            }
        }
        if (login) {
            changeWindow();
        } else {
            loginNotifier.setOpacity(1);
        }
    }
    /*User Login Controller Code End*/

    /****************************/

    /*Change Window Code Start*/
    public void changeWindow(){
        try {
            Stage stage = (Stage) userName.getScene().getWindow();
            Parent load = FXMLLoader.load(this.getClass().getResource("/com/PlayTechPvtLtd/LiveChatApplication/view/InboxManager.fxml"));
            stage.setScene(new Scene(load,330,560));
            stage.setTitle(username + "");
            stage.setOnCloseRequest(event -> {
                System.exit(0);
            });

        }catch (IOException exception){
            exception.printStackTrace();
        }
    }
    /*Change Window Code End*/

    /****************************/

    /*Set Opacity Code Start*/
    private void setOpacity(Label a, Label b, Label c, Label d) {
        if(a.getOpacity() == 1 || b.getOpacity() == 1 || c.getOpacity() == 1 || d.getOpacity() == 1) {
            a.setOpacity(0);
            b.setOpacity(0);
            c.setOpacity(0);
            d.setOpacity(0);
        }
    }
    private void setOpacity(Label controlRegLabel, Label checkEmail, Label nameExists) {
        controlRegLabel.setOpacity(0);
        checkEmail.setOpacity(0);
        nameExists.setOpacity(0);
    }
    /*Set Opacity Code End*/

    /****************************/

    /*Check User Code Start*/
    private boolean checkUser(String username) {
        for(User user : users) {
            if(user.name.equalsIgnoreCase(username)) {
                return false;
            }
        }
        return true;
    }
    /*Check User Code End*/

    /****************************/

    /*Check Mail Code Start*/
    private boolean checkEmail(String email) {
        for(User user : users) {
            if(user.email.equalsIgnoreCase(email)) {
                return false;
            }
        }
        return true;
    }
    /*Check Mail Code Start*/

    /****************************/

    /*Make Default Code Start*/
    private void makeDefault() {
        regName.setText("");
        regPass.setText("");
        regEmail.setText("");
        regFirstName.setText("");
        regPhoneNo.setText("");
        male.setSelected(true);
        setOpacity(controlRegLabel, checkEmail, nameExists);
    }
    /*Make Default Code Start*/
}
