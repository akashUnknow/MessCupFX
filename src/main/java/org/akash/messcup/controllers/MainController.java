package org.akash.messcup.controllers;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import org.akash.messcup.menuDto.MenuDto;
import org.akash.messcup.menuDto.MenuTimeDto;
import org.akash.messcup.service.MealService;
import org.akash.messcup.service.MealTime;
import org.akash.messcup.service.MenuService;
import org.akash.messcup.service.UserService;

import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    public ImageView cardImage;
    public ChoiceBox<String> idChoiceBox;
    public TextField idField;
    public TextField empNameField;
    public TextField empIdField;
    public ChoiceBox<String> mealTimeChoice;
    public Text summaryEmpName;
    public Text summaryEmpId;
    public Text summaryMeal;
    public Text summaryCount;
    public String empName;
    public Text errorMessage;
    public Text thanksMessage;
    public Button exportPdfButton;
    public TableView<MenuDto> menuTable;
    public TableColumn<MenuDto, String> day;
    public TableColumn<MenuDto, String> breakfast;
    public TableColumn<MenuDto, String> lunch;
    public TableColumn<MenuDto, String> dinner;
    public TableColumn<MenuTimeDto,String> time;
    public TableColumn<MenuTimeDto,String> meal;
    public TableView<MenuTimeDto> menuTime;
    public Button setMenuButton;



    UserService userService=new UserService();
    MealService mealService=new MealService();
    MenuService menuService=new MenuService();
    MealTime mealTime=new MealTime();



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        idChoiceBox.setValue("Employee ID");
        mealTimeChoice.setValue(MealTime.getCurrentMealTime());
        //set meal table
        time.setCellValueFactory(new PropertyValueFactory<>("time"));
        meal.setCellValueFactory(new PropertyValueFactory<>("meal"));
        List<MenuTimeDto> mealList=mealTime.setMealList();
        menuTime.getItems().setAll(mealList);

        // BIND MENU TABLE COLUMNS
        menuTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        day.setCellValueFactory(new PropertyValueFactory<>("day"));
        breakfast.setCellValueFactory(new PropertyValueFactory<>("breakfast"));
        lunch.setCellValueFactory(new PropertyValueFactory<>("lunch"));
        dinner.setCellValueFactory(new PropertyValueFactory<>("dinner"));
        List<MenuDto> menuList = menuService.loadMenu();
        menuTable.getItems().setAll(menuList);
//       logic
        idField.textProperty().addListener((_, _, newVal) -> {
            if (newVal != null && !newVal.isEmpty()) {
                empName = userService.getEmpNameById(newVal);
                empNameField.setText(empName);
                empIdField.setText(newVal);

                String result = "";
                if (mealTimeChoice.getValue().equals("No meal time currently")){
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setContentText("No meal time currently");
                    alert.showAndWait();
                    return;
                }

                if (!empName.equals("Unknown Employee") && newVal.length() >= 3) {
                    result = mealService.setCupCount(newVal, mealTimeChoice.getValue(), empName);
                }

                if(result != null && !result.isEmpty()){
                    thanksMessage.setText("");
                    errorMessage.setText(result);
                    // Show cross image
                    InputStream is = getClass().getResourceAsStream("/fxml/images/cross.jpg");
                    if(is != null) cardImage.setImage(new Image(is));
                    Platform.runLater(()->idField.setText(""));
                }
                if (result.equals("") && !empName.equals("Unknown Employee")) {
                        errorMessage.setText("");
                        thanksMessage.setText("Thank you " + empName + "!");
                        summaryEmpName.setText("Employee Name : " + empName);
                        summaryEmpId.setText("Employee ID : " + newVal);
                        summaryMeal.setText("Meal Time : " + mealTimeChoice.getValue());
                        int count = mealService.getCupCount();
                        summaryCount.setText("Number of coupons used per day : " + count);
                        // Show user image
                        InputStream is = getClass().getResourceAsStream("/fxml/images/user.jpg");
                        if(is != null) cardImage.setImage(new Image(is));
                    Platform.runLater(()->idField.setText(""));
                }
            }
        });

    }

    public void exportPdf() {
        mealService.generatePdfReport();
    }

    public void setMenu() {
        menuService.showMenu(this);

    }
    public void refreshMenuTable() {
        List<MenuDto> menuList = menuService.loadMenu();
        menuTable.getItems().setAll(menuList);
    }



}
