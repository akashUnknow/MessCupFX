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
import java.util.logging.*;

public class MenuController implements Initializable {

    /* ===================== LOGGER ===================== */
    private static final Logger LOGGER = Logger.getLogger(MenuController.class.getName());

    static {
        try {
            FileHandler fh = new FileHandler("logs/messcup.log", true);
            fh.setFormatter(new SimpleFormatter());
            LOGGER.addHandler(fh);
            LOGGER.setLevel(Level.ALL);
        } catch (Exception e) {
            e.printStackTrace(); // fallback
        }
    }

    /* ===================== UI ===================== */
    public Button saveBtn;
    public TextField dinnerField;
    public TextField lunchField;
    public TextField breakfastField;
    public ChoiceBox dayChoice;

    /* ===================== SERVICES ===================== */
    MenuService menuService = new MenuService();
    private Runnable onMenuSaved;

    private enum Days {
        Sunday, Monday, Tuesday, Wednesday, Thursday, Friday, Saturday
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        dayChoice.getItems().addAll(Days.values());
        dayChoice.setValue(Days.Monday);
        LOGGER.info("MenuController initialized, default day set to Monday");
    }

    public void setOnMenuSaved(Runnable onMenuSaved) {
        this.onMenuSaved = onMenuSaved;
        LOGGER.info("onMenuSaved callback set");
    }

    public void saveMenu() {
        try {
            LOGGER.info("Attempting to save menu for day: " + dayChoice.getValue());

            boolean success = menuService.setMenu(
                    breakfastField.textProperty(),
                    lunchField.textProperty(),
                    dinnerField.textProperty(),
                    dayChoice.getValue().toString()
            );

            if (!success) {
                LOGGER.warning("Menu save failed for day: " + dayChoice.getValue());
                return;
            }

            LOGGER.info("Menu saved successfully for day: " + dayChoice.getValue());

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Menu updated successfully!");
            alert.showAndWait();

            if (onMenuSaved != null) {
                LOGGER.info("Executing onMenuSaved callback");
                onMenuSaved.run();
            }

            Stage stage = (Stage) saveBtn.getScene().getWindow();
            stage.close();

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Exception while saving menu for day: " + dayChoice.getValue(), e);
        }
    }
}
