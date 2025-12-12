package org.akash.messcup.controllers;

import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.akash.messcup.service.MenuService;

import java.net.URL;
import java.util.ResourceBundle;

public class MenuController implements Initializable {
    public Button saveBtn;
    public TextField dinnerField;
    public TextField lunchField;
    public TextField breakfastField;
    public ChoiceBox dayChoice;
    MenuService menuService=new MenuService();
    private Runnable onMenuSaved;
    private enum Days {
        Sunday, Monday, Tuesday, Wednesday, Thursday, Friday, Saturday
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        dayChoice.getItems().addAll(Days.values());
        dayChoice.setValue(Days.Monday);

    }
    public void setOnMenuSaved(Runnable onMenuSaved) {
        this.onMenuSaved = onMenuSaved;
    }
    public void saveMenu() {
        boolean success = menuService.setMenu(
                breakfastField.textProperty(),
                lunchField.textProperty(),
                dinnerField.textProperty(),
                dayChoice.getValue().toString()
        );
        if (!success) {
            return;
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText("Menu updated successfully!");
        alert.showAndWait();
        if (onMenuSaved != null) {
            onMenuSaved.run();
        }
        Stage stage = (Stage) saveBtn.getScene().getWindow();
        stage.close();

    }
}
