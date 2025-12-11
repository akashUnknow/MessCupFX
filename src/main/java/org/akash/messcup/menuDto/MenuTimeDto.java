package org.akash.messcup.menuDto;

public class MenuTimeDto {


    private String time;
    private String meal;
    public MenuTimeDto(String time, String meal) {
        this.time = time;
        this.meal = meal;
    }

    public String getMeal() {
        return meal;
    }

    public void setMeal(String meal) {
        this.meal = meal;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
