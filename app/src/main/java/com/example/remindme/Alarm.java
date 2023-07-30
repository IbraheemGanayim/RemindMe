package com.example.remindme;

public class Alarm {
    int hour;
    int mins;
    String notes;
    String create;

    public Alarm(int hour, int mins, String notes, String create) {
        this.hour = hour;
        this.mins = mins;
        this.notes = notes;
        this.create = create;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public String getCreate() {
        return create;
    }

    public void setMins(int mins) {
        this.mins = mins;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public int getMins() {
        return mins;
    }

    public String getNotes() {
        return notes;
    }

}
