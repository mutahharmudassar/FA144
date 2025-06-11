package com.example.modifiedcinemasystem;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class CoverPageController implements Initializable {

    @FXML private ImageView logoImageView;
    @FXML private ProgressBar progressBar;
    @FXML private Label loadingLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        startLoadingAnimation();
        startAutoTransition();
    }

    private void startLoadingAnimation() {
        Timeline timeline = new Timeline();

        for (int i = 0; i <= 40; i++) {
            final double progress = i / 40.0;

            KeyFrame keyFrame = new KeyFrame(
                    Duration.millis(i * 100),
                    e -> {
                        progressBar.setProgress(progress);
                        loadingLabel.setText("Loading...");
                    }
            );
            timeline.getKeyFrames().add(keyFrame);
        }

        timeline.play();
    }

    private void startAutoTransition() {
        Timeline timeline = new Timeline(new KeyFrame(
                Duration.seconds(4),
                e -> navigateToLoginScreen()
        ));
        timeline.play();
    }

    private void navigateToLoginScreen() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("LoginScreen.fxml"));
            Stage stage = (Stage) progressBar.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Login - Cinema Management System");
        } catch (IOException e) {
            loadingLabel.setText("Error loading...");
        }
    }
}
