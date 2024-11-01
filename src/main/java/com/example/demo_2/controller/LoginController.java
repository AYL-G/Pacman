package com.example.demo_2.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.*;
import javafx.scene.control.ToggleButton;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import java.net.URL;
import java.util.ResourceBundle;

import com.example.gamedemo.Main;


public class LoginController extends CommonController {
    @FXML
    private Button logInButton;
    @FXML
    private Button signUpButton;
    @FXML
    private Label logInMessageLabel;
    @FXML
    private TextField usernameTextField;
    @FXML
    private PasswordField passwordPasswordField;
    @FXML
    private ToggleButton userToggle;
    @FXML
    private ToggleButton adminToggle;

    // Method to handle the sign-up button action
    public void signUpButtonOnAction(ActionEvent e) throws IOException {
        Stage stage = (Stage) signUpButton.getScene().getWindow();
        changeScene(stage, "signup.fxml");
    }

    // Method to handle the log-in button action
    public void logInButtonOnAction(ActionEvent e) throws SQLException {
        if(usernameTextField.getText().isBlank() || passwordPasswordField.getText().isBlank()) {
            logInMessageLabel.setText("Please enter valid username and password");
        } else {
            validatelogIn();
        }
    }

    // Method to validate log-in credentials
    public void validatelogIn() throws SQLException {
        String verifylogIn;
        String idColumn;
        if (adminToggle.isSelected()) {
            verifylogIn = "SELECT * FROM admin WHERE username = ? AND password = ?;";
            idColumn = "adminID";  // Use adminID for the admin table
        } else {
            verifylogIn = "SELECT * FROM useraccount WHERE username = ? AND password = ?;";
            idColumn = "userID";  // Use userID for the useraccount table
        }
        PreparedStatement statement = connection.prepareStatement(verifylogIn);

        statement.setString(1, usernameTextField.getText());
        statement.setString(2, passwordPasswordField.getText());

        try {
            ResultSet queryResult = statement.executeQuery();
//               <!-- if it's an admin open admin.fxml, else open userHome.fxml -->
            while (queryResult.next()) {
                if (queryResult.getInt(idColumn) > 0) {

                    setUserID(queryResult.getInt(idColumn));
//               <!-- if it's an admin open admin.fxml, else open userHome.fxml -->
                    // Check the selected role
                    if (adminToggle.isSelected()) {
                        // Check if the user is an admin
                        if (isAdmin(usernameTextField.getText())) {
                            Stage stage = (Stage) logInButton.getScene().getWindow();
                            changeScene(stage, "admin.fxml");
                        } else {
                            logInMessageLabel.setText("You are not authorized as an admin.");
                        }
                    } else {
                        // Default to user role
                        Stage stage = (Stage) logInButton.getScene().getWindow();
                        stage.setScene(Main.MSC);
                    }
                } else {
                    logInMessageLabel.setText("Invalid login. Please try again");
                }
            }
            statement.close();

        } catch (SQLException e) {
            e.printStackTrace();
            // logInMessageLabel.setText("sql exception");

        } catch (IOException e) {
            // logInMessageLabel.setText("runtime exception");
            throw new RuntimeException(e);

        }
    }
    // the admin can enter as a user too
    // Method to check if a user is an admin
    private boolean isAdmin(String username) throws SQLException {
        String adminQuery = "SELECT * FROM admin WHERE username = ?";
        PreparedStatement adminStatement = connection.prepareStatement(adminQuery);
        adminStatement.setString(1, username);
        ResultSet adminResult = adminStatement.executeQuery();
        boolean isAdmin = adminResult.next(); // If a row exists, the user is an admin
        adminStatement.close();
        return isAdmin;
    }

    // Method to handle role selection toggle buttons
    @FXML
    private void handleRoleSelection(ActionEvent event) {
        // Ensure only one toggle button is selected at a time
        if (event.getSource() == userToggle) {
            adminToggle.setSelected(false);
            addShadowEffect(userToggle);
            removeShadowEffect(adminToggle);
        } else if (event.getSource() == adminToggle) {
            userToggle.setSelected(false);
            addShadowEffect(adminToggle);
            removeShadowEffect(userToggle);
        }
    }

    // Method to add shadow effect
    private void addShadowEffect(ToggleButton button) {
        DropShadow shadow = new DropShadow();
        shadow.setColor(Color.GRAY);
        shadow.setRadius(10);
        button.setEffect(shadow);
        button.setStyle("-fx-background-color: #4Eb09B");

    }

    // Method to remove shadow effect
    private void removeShadowEffect(ToggleButton button) {
        button.setEffect(null);
        button.setStyle("-fx-background-color: transparent;");

    }

    // Method to initialize the controller
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Set initial state
        userToggle.setSelected(true);
        adminToggle.setSelected(false);
        handleRoleSelection(new ActionEvent(userToggle, null));
    }


}