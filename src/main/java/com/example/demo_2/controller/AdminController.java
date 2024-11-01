package com.example.demo_2.controller;

import com.example.demo_2.UserAccount;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.fxml.FXMLLoader;
import java.io.IOException;
import java.net.URL;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.sql.*;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class AdminController extends CommonController implements Initializable {

    // Existing fields

    @FXML
    private Button searchButton;

    @FXML
    private Button resetButton;

    @FXML
    private Button addUserButton;
    @FXML
    private Button adminButton;

    @FXML
    private Button dashboardButton;

    @FXML
    private TextField usernameSearchTextField;

    @FXML
    private TextField nameSearchTextField;

    @FXML
    private TableView<UserAccount> userAccountTableView;

    @FXML
    private TableColumn<UserAccount, Integer> userIDColumn;

    @FXML
    private TableColumn<UserAccount, String> usernameColumn;

    @FXML
    private TableColumn<UserAccount, String> passwordColumn;

    @FXML
    private TableColumn<UserAccount, String> genderColumn;

    @FXML
    private TableColumn<UserAccount, String> firstNameColumn;

    @FXML
    private TableColumn<UserAccount, String> lastNameColumn;

    @FXML
    private TableColumn<UserAccount, String> DOBColumn;

    @FXML
    private TableColumn<UserAccount, String> createdDateColumn;

    private ObservableList<UserAccount> userAccountList;

    // Database connection fields
    private Connection connection;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        userAccountList = FXCollections.observableArrayList();

        // Initialize TableView columns
//        userIDColumn.setCellValueFactory(new PropertyValueFactory<>("userID"));
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        passwordColumn.setCellValueFactory(new PropertyValueFactory<>("password"));
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        genderColumn.setCellValueFactory(new PropertyValueFactory<>("gender"));
        DOBColumn.setCellValueFactory(new PropertyValueFactory<>("dob"));
        createdDateColumn.setCellValueFactory(new PropertyValueFactory<>("createdDate"));

        userAccountTableView.setItems(userAccountList);

        // Initialize database connection
        initializeDatabase();

        // Load data into TableView
        loadDataFromDatabase();
    }

    private void initializeDatabase() {
        // Initialize your database connection here
        String url = "jdbc:mysql://localhost:3306/demo_db";
        String username = "root";
        String password = "nico2001";

        try {
            connection = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle any SQL exception
        }
    }

    private void loadDataFromDatabase() {
        // Fetch data from useraccount table and populate TableView
        String query = "SELECT * FROM useraccount";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                int userID = rs.getInt("userID");
                String username = rs.getString("username");
                String password = rs.getString("password");
                String firstName = rs.getString("firstname");
                String lastName = rs.getString("lastname");
                String gender = rs.getString("gender");
                String dob = rs.getString("dob");
                String createdDate = rs.getString("createdDate");

                UserAccount user = new UserAccount(userID, username, password, firstName, lastName, gender, dob, createdDate);
                userAccountList.add(user);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            // Handle any SQL exception
        }
    }

    @FXML
    public void addUserButtonOnAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/demo_2/add.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) addUserButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void deleteUserButtonOnAction(ActionEvent event) {
        UserAccount selectedUser = userAccountTableView.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            try {
                // Check if the user is an admin
                String checkAdminQuery = "SELECT * FROM admin WHERE adminID = ?";
                PreparedStatement checkAdminStatement = connection.prepareStatement(checkAdminQuery);
                checkAdminStatement.setInt(1, selectedUser.getUserID());
                ResultSet adminResultSet = checkAdminStatement.executeQuery();

                if (adminResultSet.next()) {
                    // Delete from admin table
                    String deleteAdminQuery = "DELETE FROM admin WHERE adminID = ?";
                    PreparedStatement deleteAdminStatement = connection.prepareStatement(deleteAdminQuery);
                    deleteAdminStatement.setInt(1, selectedUser.getUserID());
                    deleteAdminStatement.executeUpdate();
                }

                // Delete from useraccount table
                String deleteUserQuery = "DELETE FROM useraccount WHERE userID = ?";
                PreparedStatement deleteStatement = connection.prepareStatement(deleteUserQuery);
                deleteStatement.setInt(1, selectedUser.getUserID());
                deleteStatement.executeUpdate();

                // Remove from userAccountList and refresh TableView
                userAccountList.remove(selectedUser);
            } catch (SQLException e) {
                e.printStackTrace();
                // Handle exceptions
            }
        }
    }

    @FXML
    public void backButtonOnAction(ActionEvent e) throws IOException {
        super.backButtonOnAction(e, "login.fxml");
    }

    @FXML
    public void searchButtonOnAction(ActionEvent e) throws SQLException {
        search();
    }

    @FXML
    public void resetButtonOnAction() {
        usernameSearchTextField.setText("");
        nameSearchTextField.setText("");
        reset();
    }

    public void reset() {
        userAccountList.clear();
        loadDataFromDatabase();
    }

    public void search() throws SQLException {
        userAccountList.clear();

        String queryUser = "SELECT * FROM useraccount WHERE username LIKE '%" +
                usernameSearchTextField.getText() +
                "%' AND (lastname LIKE '%" +
                nameSearchTextField.getText() +
                "%' OR firstname LIKE '%" +
                nameSearchTextField.getText() +
                "%') LIMIT 20;";

        try (PreparedStatement statement = connection.prepareStatement(queryUser);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                int userID = resultSet.getInt("userID");
                String username = resultSet.getString("username");
                String password = resultSet.getString("password");
                String firstName = resultSet.getString("firstname");
                String lastName = resultSet.getString("lastname");
                String gender = resultSet.getString("gender");
                String dob = resultSet.getString("dob");
                String createdDate = resultSet.getString("createdDate");

                UserAccount userAccount = new UserAccount(userID, username, password, firstName, lastName, gender, dob, createdDate);
                userAccountList.add(userAccount);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            // Handle SQL exception
        }


    }

    @FXML
    public void dashboardButtonOnAction(ActionEvent event) {
        // Load and open the new page (Dashboard)
        try {
            Stage stage = (Stage) dashboardButton.getScene().getWindow();
            changeScene(stage,"adminDashboard.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
