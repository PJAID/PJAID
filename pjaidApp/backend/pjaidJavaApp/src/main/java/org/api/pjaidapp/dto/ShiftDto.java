package org.api.pjaidapp.dto;

public class ShiftDto {
    public String technicianName;
    public String shiftDate;
    public String shiftTime;

    public ShiftDto(String name, String date, String time) {
        this.technicianName = name;
        this.shiftDate = date;
        this.shiftTime = time;
    }
}