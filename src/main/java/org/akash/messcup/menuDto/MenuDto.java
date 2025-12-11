package org.akash.messcup.menuDto;


public class MenuDto {
    private final String day;
    private final String breakfast;
    private final String lunch;
    private final String dinner;

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
