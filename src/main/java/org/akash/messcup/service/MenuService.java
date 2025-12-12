package org.akash.messcup.service;


import javafx.beans.property.StringProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.akash.messcup.controllers.MainController;
import org.akash.messcup.controllers.MenuController;
import org.akash.messcup.menuDto.MenuDto;

import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;


public class MenuService {
    String filePath="C:/Mess/menu.txt";

    public List<MenuDto> loadMenu() {
        List<MenuDto> menuList=new ArrayList<>();

        try {
            List<String> lines= Files.readAllLines(Paths.get(filePath));

            for (String line:lines){
                String[] parts=line.split("\\|");
                if(parts.length==4){
                    menuList.add(new MenuDto(parts[0], parts[1], parts[2], parts[3]));
                }
            }
            return menuList;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void showMenu(MainController mainController) {
        try {
            FXMLLoader loader=new FXMLLoader(getClass().getResource("/fxml/menu.fxml"));
            Scene scene=new Scene(loader.load());
            MenuController controller = loader.getController();
            controller.setOnMenuSaved(mainController::refreshMenuTable);
            Stage stage=new Stage();
            stage.setScene(scene);
            stage.setTitle("Mess Cup");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public Boolean setMenu(StringProperty brk, StringProperty luc, StringProperty din, String day) {
        try {
            if (day == null || day.trim().isEmpty()) {
                showWarning("Day cannot be empty.");
                return false;
            }
            if (brk.get() == null || brk.get().trim().isEmpty()) {
                showWarning("Breakfast cannot be empty.");
                return false;
            }
            if (luc.get() == null || luc.get().trim().isEmpty()) {
                showWarning("Lunch cannot be empty.");
                return false;
            }
            if (din.get() == null || din.get().trim().isEmpty()) {
                showWarning("Dinner cannot be empty.");
                return false;
            }
            Path path = Paths.get(filePath);
            List<String> lines = Files.readAllLines(path);

            String newEntry = day + "|" + brk.get() + "|" + luc.get() + "|" + din.get();

            // Find the index of the line to replace
            int index = -1;
            for (int i = 0; i < lines.size(); i++) {
                if (lines.get(i).startsWith(day + "|")) {
                    index = i;
                    break;
                }
            }

            // Replace OR Add at correct index
            if (index != -1) {
                // Replace existing line
                lines.set(index, newEntry);
            } else {
                // Day not found → add at the end
                lines.add(newEntry);
            }

            Files.write(path, lines);
            return true; // success

        }catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    private void showWarning(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Validation Failed");
        alert.setHeaderText("Invalid Menu Data");
        alert.setContentText(message);
        alert.showAndWait();
    }

}
