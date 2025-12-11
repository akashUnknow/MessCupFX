package org.akash.messcup.menuDto;


public class MenuDto {
    private String day;
    private String breakfast;
    private String lunch;
    private String dinner;

    public MenuDto(String day, String breakfast, String lunch, String dinner) {
        this.day = day;
        this.breakfast = breakfast;
        this.lunch = lunch;
        this.dinner = dinner;
    }
    public String getDay() { return day; }
    public String getBreakfast() { return breakfast; }
    public String getLunch() { return lunch; }
    public String getDinner() { return dinner; }
    // getters
}
