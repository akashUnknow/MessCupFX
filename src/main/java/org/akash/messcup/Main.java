package org.akash.messcup;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.*;

public class Main extends Application {

    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());

    static {
        try {
            Files.createDirectories(Paths.get("logs"));
            FileHandler fh = new FileHandler("logs/messcup.log", 0, 1, true);
            fh.setFormatter(new SimpleFormatter());
            LOGGER.addHandler(fh);
            LOGGER.setLevel(Level.ALL);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        LOGGER.info("Application starting...");
        launch();
    }

    @Override
    public void start(Stage stage) {
        try {
            LOGGER.info("Loading main.fxml...");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/main.fxml"));
            Scene scene = new Scene(loader.load());
            stage.setMaximized(true);
            stage.setScene(scene);
            stage.setTitle("Mess Cup");
            stage.show();
            LOGGER.info("Application started successfully");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to start application", e);
        }
    }
}
