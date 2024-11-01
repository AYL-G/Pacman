package com.example.demo_2.controller;

import javafx.beans.value.ObservableListValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class AddController extends CommonController {
    @FXML
    private Button registerButton;
    @FXML
    private Label signUpMessageLabel;
    @FXML
    private TextField usernameTextField;
    @FXML
    private PasswordField passwordPasswordField;
    @FXML
    private TextField firstNameTextField;
    @FXML
    private TextField lastNameTextField;
    @FXML
    private ComboBox<String> genderComboBox;

    ObservableList<String> genderList = FXCollections.observableArrayList("Male", "Female");
    @FXML
    private DatePicker dobDatePicker;

    // Method to handle the back button action
    public void backButtonOnAction(ActionEvent e) throws IOException {
        super.backButtonOnAction(e, "admin.fxml");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        genderComboBox.setItems(genderList);
    }

    // Method to handle the register button action
    public void registerButtonOnAction(ActionEvent e) throws IOException, InterruptedException {
        if(usernameTextField.getText().isBlank()
                || passwordPasswordField.getText().isBlank()
                || lastNameTextField.getText().isBlank()
                || firstNameTextField.getText().isBlank()
                || genderComboBox.getValue().isBlank() ) {
            signUpMessageLabel.setText("Please enter all the fields!");
        } else {
            // Validate registration and change scene
            validateRegister();
            Stage stage = (Stage) registerButton.getScene().getWindow();
            changeScene(stage, "admin.fxml");

        }
    }

    public void validateRegister() {
        // SQL insert statement for SQL Server
        String sqlDML = "INSERT INTO useraccount (username, password, firstName, lastName, gender, dob) " +
                "VALUES (?,?,?,?,?,?)";
        try {
            PreparedStatement statement = connection.prepareStatement(sqlDML);
            statement.setString(3, firstNameTextField.getText());
            statement.setString(4, lastNameTextField.getText());
            statement.setString(1, usernameTextField.getText());
            statement.setString(2, passwordPasswordField.getText());
            statement.setString(5, genderComboBox.getValue());
            statement.setString(6, String.valueOf(dobDatePicker.getValue()));
            signUpMessageLabel.setText("User added!");
            // Execute the SQL statement
            int insertResult = statement.executeUpdate();

            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            signUpMessageLabel.setText("Username has already existed!");
        }
    }


}
