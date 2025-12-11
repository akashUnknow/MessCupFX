package org.akash.messcup.service;

import java.time.LocalTime;

public class MealTime {
    public enum Time {
        BREAKFAST,
        LUNCH,
        DINNER
    }
    public static String getCurrentMealTime() {
        LocalTime time = LocalTime.now();
        if (time.isAfter(LocalTime.of(6, 0)) && time.isBefore(LocalTime.of(10, 0))) {
            return Time.BREAKFAST.name();
        } else if (time.isAfter(LocalTime.of(12, 0)) && time.isBefore(LocalTime.of(15, 0))) {
            return Time.LUNCH.name();
        } else if (time.isAfter(LocalTime.of(18, 0)) && time.isBefore(LocalTime.of(21, 0))) {
            return Time.DINNER.name();
        } else {
            return "No meal time currently";
        }
    }
}
