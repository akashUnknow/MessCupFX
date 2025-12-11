package org.akash.messcup.model;

public class MealCup {
    private String empId;
    private String empName;
    private String mealTime;
    private int cupCount;

    public MealCup(String empId, String empName, String mealTime, int cupCount) {
        this.empId = empId;
        this.empName = empName;
        this.mealTime = mealTime;
        this.cupCount = cupCount;
    }

    public String getEmpId() {
        return empId;
    }

    public String getEmpName() {
        return empName;
    }

    public String getMealTime() {
        return mealTime;
    }

    public int getCupCount() {
        return cupCount;
    }
}
