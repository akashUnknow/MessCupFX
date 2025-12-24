package org.akash.messcup.service;

import org.akash.messcup.menuDto.MenuTimeDto;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class MealTime {

    public enum Time {
        BREAKFAST,
        LUNCH,
        DINNER
    }
    public List<MenuTimeDto> setMealList() {
        List<MenuTimeDto> menuTime=new ArrayList<>();
        menuTime.add(new MenuTimeDto("BREAKFAST","6 - 10"));
        menuTime.add(new MenuTimeDto("LUNCH","12 - 15"));
        menuTime.add(new MenuTimeDto("DINNER","18 - 22"));

        return menuTime;
    }
    public static String getCurrentMealTime() {
        LocalTime time = LocalTime.now();
        if (time.isAfter(LocalTime.of(6, 0)) && time.isBefore(LocalTime.of(10, 0))) {
            return Time.BREAKFAST.name();
        } else if (time.isAfter(LocalTime.of(12, 0)) && time.isBefore(LocalTime.of(15, 0))) {
            return Time.LUNCH.name();
        } else if (time.isAfter(LocalTime.of(18, 0)) && time.isBefore(LocalTime.of(23, 0))) {
            return Time.DINNER.name();
        } else {
            return "No meal time currently";
        }
    }
}
