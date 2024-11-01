package com.example.demo_2;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class DatabaseConnection {
    public Connection databaseLink;
    private String databaseName;
    private String databaseUser;
    private String databasePassword;
    // Method to establish the connection with the database
    public Connection getConnection() {
        String path = "src/main/resources/config.properties";
        getDataFromConfigFile(path);

        // Update JDBC URL for MySQL
        String url = "jdbc:mysql://localhost:3306/" + databaseName;

        try {
            // Load MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Establish connection
            databaseLink = DriverManager.getConnection(url, databaseUser, databasePassword);
            // System.out.println("Connected to MySQL database."); // to check that everything is okay

        } catch (Exception e) {
            e.printStackTrace();
        }
        return databaseLink;
    }

    public void getDataFromConfigFile(String path) {
        try {
            FileInputStream fis = new FileInputStream(path);
            Properties prop = new Properties();
            prop.load(fis);
            databaseUser = prop.getProperty("username");
            databasePassword = prop.getProperty("password");
            databaseName =  prop.getProperty("databaseName");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
