package org.akash.messcup;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    static void main() {
        launch();
    }

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader=new FXMLLoader(getClass().getResource("/fxml/main.fxml"));
        Scene scene=new Scene(loader.load());
        stage.setMaximized(true);
        stage.setScene(scene);
        stage.setTitle("Mess Cup");
        stage.show();
    }
}
