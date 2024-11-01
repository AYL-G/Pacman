package com.example.demo_2;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import java.io.IOException;

public class AppMain /*extends Application*/ {
    public static FXMLLoader fxmlLoader_userhome = new FXMLLoader(AppMain.class.getResource("login.fxml"));
    public static Scene stager() throws IOException {

        Scene scene = new Scene(fxmlLoader_userhome.load(), 600, 400);
        Image icon_ = new Image("file:src/main/resources/com/example/img/icons8-login-64.png");

        return scene;
    }
}