package com.example.demo_2.controller;

import com.example.demo_2.UserAccount;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class AdminDashboardController extends CommonController implements Initializable {
    @FXML
    private Label totalUsersLabel;
    @FXML
    private PieChart userGenderChart;
    @FXML
    private Button adminButton;

    @FXML
    private Button dashboardButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadUserStatistics();
    }

    private void loadUserStatistics() {
        try {
            String query = "SELECT gender, COUNT(*) as count FROM useraccount GROUP BY gender";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
            int totalUsers = 0;
            while (resultSet.next()) {
                String gender = resultSet.getString("gender");
                int count = resultSet.getInt("count");
                pieChartData.add(new PieChart.Data(gender, count));
                totalUsers += count;
            }
            userGenderChart.setData(pieChartData);
            totalUsersLabel.setText(String.valueOf(totalUsers));
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error SQL");
        }
    }

    @FXML
    public void adminButtonOnAction(ActionEvent event) {
        // Load and open the new page (Dashboard)
        try {
            Stage stage = (Stage) adminButton.getScene().getWindow();
            changeScene(stage, "admin.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void quitButtonOnAction(ActionEvent e) {
        Stage stage = (Stage) quitButton.getScene().getWindow();
        stage.close();
    }
}
