package org.akash.messcup.controllers;

import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import org.akash.messcup.service.MealService;
import org.akash.messcup.service.MealTime;
import org.akash.messcup.service.UserService;

import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    public ImageView cardImage;
    public ChoiceBox idChoiceBox;
    public TextField idField;
    public TextField empNameField;
    public TextField empIdField;
    public ChoiceBox mealTimeChoice;
    public Text summaryEmpName;
    public Text summaryEmpId;
    public Text summaryMeal;
    public Text summaryCount;
    public String empName;

    UserService userService=new UserService();
    MealService mealService=new MealService();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        idChoiceBox.setValue("Employee ID");
        mealTimeChoice.setValue(MealTime.getCurrentMealTime());
        idField.textProperty().addListener((obs,oldval,newVal)->{
            if(newVal!=null && !newVal.isEmpty()) {
                empName = userService.getEmpNameById(newVal);
                empNameField.setText(empName);
                empIdField.setText(newVal);
                if (!empName.equals("Unknown Employee") && newVal.length()>=3) {
                    mealService.setCupCount(newVal, mealTimeChoice.getValue().toString(), empName);
                }
            }
        });
    }

}
