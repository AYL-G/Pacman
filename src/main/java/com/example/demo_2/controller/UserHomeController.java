package com.example.demo_2.controller;

import com.example.demo_2.AppMain;
import com.example.demo_2.UserAccount;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class UserHomeController extends CommonController implements Initializable {
    @FXML
    private Label usernameLabel;
    @FXML
    private Label firstnameLabel;
    @FXML
    private Label lastnameLabel;
    @FXML
    private Label genderLabel;
    @FXML
    private Label dobLabel;
    @FXML
    private Label fullnameTextField;

    @FXML
    private TextField scoreTextField;
    @FXML
    private Button pushButton;
    public void backButtonOnAction(ActionEvent e) throws IOException {
        super.backButtonOnAction(e, "login.fxml");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            loadUserInfo();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void loadUserInfo() throws SQLException {
        String loadUserDataQuery = "SELECT * FROM useraccount WHERE userID = ?;";
        PreparedStatement statement = connection.prepareStatement(loadUserDataQuery);
        statement.setString(1, String.valueOf(userID));
        try {
            ResultSet queryResult = statement.executeQuery();
            queryResult.next();
            // get String from column in Database
            usernameLabel.setText(queryResult.getString("username"));
            firstnameLabel.setText(queryResult.getString("firstName"));
            lastnameLabel.setText(queryResult.getString("lastName"));
            genderLabel.setText(queryResult.getString("gender"));
            dobLabel.setText(queryResult.getString("dob"));

            fullnameTextField.setText(firstnameLabel.getText() + ' ' + lastnameLabel.getText());
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void scoreTextFieldOnAction(ActionEvent e) {

    }

    public void pushButtonOnAction(ActionEvent e) {
        pushData();
        scoreTextField.setText("");
    }
    public void pushData(){
//        String sqlDML = "INSERT INTO gamescore (userID, score) VALUE (?,?)";
//        try {
//            PreparedStatement statement = connection.prepareStatement(sqlDML);
//            statement.setString(1, String.valueOf(userID));
//            statement.setString(2, scoreTextField.getText());
//            int insertResult = statement.executeUpdate();
//            if(insertResult > 0) System.out.println( "oke roi ban oi");
//            statement.close();
//        } catch (SQLException e) {
//            e.printStackTrace();
//            System.out.println( "khong on ròi");
//        }
    }

}
