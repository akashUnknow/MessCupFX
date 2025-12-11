package org.akash.messcup.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import org.akash.messcup.menuDto.MenuDto;
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
    public Text errorMessage;
    public Text ThanksMessage;
    public Button exportPdfButton;
    public TableView<MenuDto> menuTable;
    public TableColumn<MenuDto, String> Day;
    public TableColumn<MenuDto, String> Breakfast;
    public TableColumn<MenuDto, String> Lunch;
    public TableColumn<MenuDto, String> Dinner;


    UserService userService=new UserService();
    MealService mealService=new MealService();
    MenuService menuService=new MenuService();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        idChoiceBox.setValue("Employee ID");
        mealTimeChoice.setValue(MealTime.getCurrentMealTime());
        // BIND MENU TABLE COLUMNS
        menuTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);


        Day.setCellValueFactory(new PropertyValueFactory<>("day"));
        Breakfast.setCellValueFactory(new PropertyValueFactory<>("breakfast"));
        Lunch.setCellValueFactory(new PropertyValueFactory<>("lunch"));
        Dinner.setCellValueFactory(new PropertyValueFactory<>("dinner"));

        List<MenuDto> menuList = menuService.setMenu();
        menuTable.getItems().setAll(menuList);

        menuTable.getItems().setAll(menuList);
        idField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null && !newVal.isEmpty()) {
                empName = userService.getEmpNameById(newVal);
                empNameField.setText(empName);
                empIdField.setText(newVal);

                String result = "";
                if (mealTimeChoice.getValue().toString().equals("No meal time currently")){
                    System.out.println("return;");
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setContentText("No meal time currently");
                    alert.showAndWait();
                    return;
                }

                if (!empName.equals("Unknown Employee") && newVal.length() >= 3) {
                    result = mealService.setCupCount(newVal, mealTimeChoice.getValue().toString(), empName);
                }

                if(result != null && !result.isEmpty()){
                    ThanksMessage.setText("");
                    errorMessage.setText(result);
                    // Show cross image
                    InputStream is = getClass().getResourceAsStream("/fxml/images/cross.jpg");
                    if(is != null) cardImage.setImage(new Image(is));
                    idField.setText("");
                }
                if (result.equals("") && !empName.equals("Unknown Employee")) {
                        errorMessage.setText("");
                        ThanksMessage.setText("Thank you " + empName + "!");
                        summaryEmpName.setText("Employee Name : " + empName);
                        summaryEmpId.setText("Employee ID : " + newVal);
                        summaryMeal.setText("Meal Time : " + mealTimeChoice.getValue().toString());
                        int count = mealService.getCupCount();
                        summaryCount.setText("Number of coupons used per day : " + count);

                        // Show user image
                        InputStream is = getClass().getResourceAsStream("/fxml/images/user.jpg");
                        if(is != null) cardImage.setImage(new Image(is));


                }
            }
        });

    }

    public void exportPdf(ActionEvent actionEvent) {
        mealService.generatePdfReport();
    }
}
