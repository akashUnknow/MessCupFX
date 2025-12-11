package org.akash.messcup.service;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.util.Date;

public class MealService {
    private int cupCount=1;
    private String path="C:/Mess/mealData.txt";
    LocalDate today = LocalDate.now();
    public void setCupCount(String id, String mealTime, String empName) {
        if(empName.equals("Unknown Employee")){
            return;
        }
        // Implementation to set cup count
        try {
            String entry = id + "|" + empName + "|" + mealTime +"|"+ today +"|"+ cupCount + "\n";
            Files.write(Paths.get(path),entry.getBytes(), StandardOpenOption.APPEND);

        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
