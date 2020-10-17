package com.example.manager.model;

public class Schedule {
    public String date;
    public boolean dayShift;
    public boolean nightShift;

    public Schedule() {
    }

    public Schedule(String date, boolean dayShift, boolean nightShift) {
        this.date = date;
        this.dayShift = dayShift;
        this.nightShift = nightShift;
    }

    public boolean isDayShift() {
        return dayShift;
    }

    public void setDayShift(boolean dayShift) {
        this.dayShift = dayShift;
    }

    public boolean isNightShift() {
        return nightShift;
    }

    public void setNightShift(boolean nightShift) {
        this.nightShift = nightShift;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}

