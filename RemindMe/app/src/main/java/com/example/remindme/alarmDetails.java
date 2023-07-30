package com.example.remindme;

import androidx.room.ColumnInfo;
import androidx.room.Entity;

@Entity(primaryKeys = {"hour","minute"})
public class alarmDetails {
    @ColumnInfo(name = "hour")
    public int hour;
    @ColumnInfo(name = "minute")
    public int minute;

    @ColumnInfo(name = "Note")
    public String alarmNote;

    @ColumnInfo(name = "time_create")
    public String create;

    public alarmDetails(int hour, int minute, String alarmNote, String create) {
        this.hour = hour;
        this.minute = minute;
        this.alarmNote = alarmNote;
        this.create = create;
    }

}
