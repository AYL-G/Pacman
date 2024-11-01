module com.example.pac_man {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires com.almasb.fxgl.all;
    requires javafx.media;

    requires java.sql;
    requires mysql.connector.java;
    requires lombok;

    opens com.example.gamedemo to javafx.fxml;
    exports com.example.gamedemo;

    //Part of Login
    opens com.example.demo_2 to javafx.fxml;
    exports com.example.demo_2;
    exports com.example.demo_2.controller;
    opens com.example.demo_2.controller to javafx.fxml;

}