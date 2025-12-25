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
import org.akash.messcup.service.*;

import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.*;

public class MainController implements Initializable {

    /* ===================== LOGGER ===================== */
    private static final Logger LOGGER = Logger.getLogger(MainController.class.getName());

    static {
        try {
            Files.createDirectories(Path.of("logs"));
            FileHandler fileHandler = new FileHandler("logs/messcup.log", 0, 1, true);
            fileHandler.setFormatter(new SimpleFormatter());
            LOGGER.addHandler(fileHandler);
            LOGGER.setLevel(Level.ALL);
        } catch (Exception e) {
            e.printStackTrace(); // last fallback
        }
    }

    /* ===================== UI ===================== */
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

    /* ===================== SERVICES ===================== */
    UserService userService = new UserService();
    MealService mealService = new MealService();
    MenuService menuService = new MenuService();
    MealTime mealTime = new MealTime();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        LOGGER.info("MessCup application started");

        // Catch ALL uncaught exceptions (VERY IMPORTANT FOR EXE)
        Thread.setDefaultUncaughtExceptionHandler((t, e) -> {
            LOGGER.log(Level.SEVERE, "Unhandled exception in thread: " + t.getName(), e);
        });

        try {
            idChoiceBox.setValue("Employee ID");
            mealTimeChoice.setValue(MealTime.getCurrentMealTime());

            /* ===================== CARD READER ===================== */
            CardReader cardReader = new CardReader(uid -> {

                if ("NO_READER".equals(uid) || "ERROR".equals(uid)) {
                    LOGGER.warning("Card reader error");
                    Platform.runLater(() -> errorMessage.setText("Card reader error"));
                    return;
                }

                Platform.runLater(() -> idField.setText(uid));
            });

            Thread cardThread = new Thread(cardReader, "CardReader-Thread");
            cardThread.setDaemon(true);
            cardThread.start();

            /* ===================== MEAL TIME TABLE ===================== */
            time.setCellValueFactory(new PropertyValueFactory<>("time"));
            meal.setCellValueFactory(new PropertyValueFactory<>("meal"));
            menuTime.getItems().setAll(mealTime.setMealList());

            /* ===================== MENU TABLE ===================== */
            menuTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
            day.setCellValueFactory(new PropertyValueFactory<>("day"));
            breakfast.setCellValueFactory(new PropertyValueFactory<>("breakfast"));
            lunch.setCellValueFactory(new PropertyValueFactory<>("lunch"));
            dinner.setCellValueFactory(new PropertyValueFactory<>("dinner"));
            menuTable.getItems().setAll(menuService.loadMenu());

            /* ===================== MAIN LOGIC ===================== */
            idField.textProperty().addListener((a, b, newVal) -> {
                if (newVal == null || newVal.isEmpty()) return;

                try {

                    String empName = userService.getEmpNameById(newVal);
                    empNameField.setText(empName);
                    empIdField.setText(newVal);

                    if ("No meal time currently".equals(mealTimeChoice.getValue())) {
                        new Alert(Alert.AlertType.INFORMATION, "No meal time currently").showAndWait();
                        return;
                    }

                    String result = "";
                    if (!"Unknown Employee".equals(empName) && newVal.length() >= 3) {
                        result = mealService.setCupCount(newVal, mealTimeChoice.getValue(), empName);
                    }

                    if (result != null && !result.isEmpty()) {
                        errorMessage.setText(result);
                        thanksMessage.setText("");
                        loadImage("/fxml/images/cross.jpg");
                        Platform.runLater(() -> idField.setText(""));
                    } else if (!"Unknown Employee".equals(empName)) {
                        errorMessage.setText("");
                        thanksMessage.setText("Thank you " + empName + "!");
                        summaryEmpName.setText("Employee Name : " + empName);
                        summaryEmpId.setText("Employee ID : " + newVal);
                        summaryMeal.setText("Meal Time : " + mealTimeChoice.getValue());
                        summaryCount.setText("Number of coupons used per day : " + mealService.getCupCount());
                        loadImage("/fxml/images/user.jpg");
                        Platform.runLater(() -> idField.setText(""));
                    }

                } catch (Exception ex) {
                    LOGGER.log(Level.SEVERE, "Error processing employee ID", ex);
                }
            });

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Initialization failed", e);
        }
    }

    private void loadImage(String path) {
        try (InputStream is = getClass().getResourceAsStream(path)) {
            if (is != null) cardImage.setImage(new Image(is));
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Failed to load image: " + path, e);
        }
    }

    public void exportPdf() {
        try {
            LOGGER.info("Export PDF clicked");
            mealService.generatePdfReport();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "PDF generation failed", e);
        }
    }

    public void setMenu() {
        menuService.showMenu(this);
    }

    public void refreshMenuTable() {
        menuTable.getItems().setAll(menuService.loadMenu());
    }
}
