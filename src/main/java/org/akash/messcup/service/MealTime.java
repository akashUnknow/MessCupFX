package org.akash.messcup.service;

import org.akash.messcup.menuDto.MenuTimeDto;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.*;

public class MealTime {

    private static final Logger LOGGER = Logger.getLogger(MealTime.class.getName());

    static {
        try {
            java.nio.file.Files.createDirectories(java.nio.file.Paths.get("logs"));
            FileHandler fh = new FileHandler("logs/messcup.log", true);
            fh.setFormatter(new SimpleFormatter());
            LOGGER.addHandler(fh);
            LOGGER.setLevel(Level.ALL);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public enum Time {
        BREAKFAST,
        LUNCH,
        DINNER
    }

    public List<MenuTimeDto> setMealList() {
        List<MenuTimeDto> menuTime = new ArrayList<>();
        menuTime.add(new MenuTimeDto("BREAKFAST", "6 - 10"));
        menuTime.add(new MenuTimeDto("LUNCH", "12 - 15"));
        menuTime.add(new MenuTimeDto("DINNER", "18 - 22"));
        LOGGER.info("Meal list initialized with BREAKFAST, LUNCH, DINNER time ranges");
        return menuTime;
    }

    public static String getCurrentMealTime() {
        LocalTime time = LocalTime.now();
        String currentMeal;
        if (time.isAfter(LocalTime.of(6, 0)) && time.isBefore(LocalTime.of(10, 0))) {
            currentMeal = Time.BREAKFAST.name();
        } else if (time.isAfter(LocalTime.of(12, 0)) && time.isBefore(LocalTime.of(15, 0))) {
            currentMeal = Time.LUNCH.name();
        } else if (time.isAfter(LocalTime.of(18, 0)) && time.isBefore(LocalTime.of(23, 0))) {
            currentMeal = Time.DINNER.name();
        } else {
            currentMeal = "No meal time currently";
        }
        LOGGER.info("Current meal time determined: " + currentMeal);
        return currentMeal;
    }
}
