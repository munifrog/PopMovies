package com.example.popularmovies.model;

import android.arch.persistence.room.TypeConverter;

import java.util.Calendar;

public class CalendarConverter {
    @TypeConverter
    public static Calendar convertToCalendar(Long millis) {
        Calendar calendar = null;
        if (millis != null) {
            calendar = Calendar.getInstance();
            calendar.setTimeInMillis(millis);
        }
        return calendar;
    }

    @TypeConverter
    public static long convertToLong(Calendar calendar) {
        Long newLong = null;
        if (calendar != null) {
            newLong = calendar.getTimeInMillis();
        }
        return newLong;
    }
}
