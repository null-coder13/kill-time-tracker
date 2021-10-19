package com.killtimetracker.localstorage;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class KillTimeEntry
{
    public final String name;
    public final int killCount;
    //TODO: need methods to convert times for calcutations
    public final int hour;
    public final int minute;
    public final int second;
    public Date date;

    public int convertToSeconds()
    {
        int hoursToSeconds = this.hour*60*60;
        int minutesToSeconds = this.minute*60;
        return hoursToSeconds + minutesToSeconds + second;
    }
}

