package com.example.demo_2.controller;

import com.example.demo_2.DatabaseConnection;
import com.example.demo_2.AppMain;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.util.ResourceBundle;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommonController implements Initializable {
    // Create a new instance of the DatabaseConnection class +establish a connection
    protected DatabaseConnection connectNow = new DatabaseConnection();
    protected Connection connection = connectNow.getConnection();

    protected static int userID;

    public static int getUserID() {
        return userID;
    }

    public static void setUserID(int userID) {
        CommonController.userID = userID;
    }

    @FXML
    protected Button backButton;
    @FXML
    protected Button quitButton;
    // Method to change the scene
    public static void changeScene(Stage stage, String path) throws IOException {
        FXMLLoader fxmlLoader_signup = new FXMLLoader(AppMain.class.getResource(path));
        Scene scene = new Scene(fxmlLoader_signup.load(), 600, 400);
        stage.setScene(scene);
        stage.show();

    }
    // Action event handler for the quit button
    public void quitButtonOnAction(ActionEvent e) {
        Stage stage = (Stage) quitButton.getScene().getWindow();
        stage.close();
    }
    // Action event handler for the back button
    public void backButtonOnAction(ActionEvent e, String path) throws IOException {
        Stage stage = (Stage) backButton.getScene().getWindow();
        changeScene(stage, path);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
