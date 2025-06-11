package com.example.modifiedcinemasystem;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        System.out.println("Starting OOPsters Cinema Management System");
        System.out.println("Current Date and Time (UTC - YYYY-MM-DD HH:MM:SS formatted): 2025-06-05 21:40:59");

        // Load the cover page first
        Parent root = FXMLLoader.load(getClass().getResource("CoverPage.fxml"));

        primaryStage.setTitle("OOPsters Cinema Management System");
        primaryStage.setScene(new Scene(root));
        primaryStage.setResizable(false);
        primaryStage.show();

        System.out.println("Cover page loaded successfully");
    }

    public static void main(String[] args) {
        launch(args);
    }
}