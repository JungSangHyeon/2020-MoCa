package com.onandon.moca.model;

import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.onandon.moca.Constant;
import com.onandon.moca.utility.UWeek;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MTime implements Serializable, Cloneable {

    private long timeInMillis;

    private boolean bRecurring;
    private boolean bHolidayOff; // Holiday Alarm Off
    private final boolean[] bDayOfWeeks;

    @NonNull
    @Override
    public MTime clone() {
        try {
            return (MTime) super.clone();
        } catch (CloneNotSupportedException e) {
            return new MTime();
        }
    }

    public MTime() {
        // time
        Calendar calendar = Calendar.getInstance();
        this.timeInMillis = calendar.getTimeInMillis();

        // day of week
        this.bDayOfWeeks = new boolean[Constant.EWeekDay.values().length];
        // mon = 0 .. sun = 6
        for (int i = 0; i< Constant.EWeekDay.values().length; i++) {
            this.bDayOfWeeks[i] = false;
        }
        Constant.EWeekDay todayOfWeek = this.getDayOfWeek();
        this.setDayOfWeekChecked(todayOfWeek.ordinal(), true);
        this.setRecurringChecked(true);


        // holiday
        this.setHolidayOffChecked(false);
    }

    // time
    public long getTimeInMillis() {
        return this.timeInMillis;
    }

    public int getHourOfDay() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(this.timeInMillis);
        return calendar.get(Calendar.HOUR_OF_DAY);
    }
    public void setHourOfDay(int hourOfDay) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(this.timeInMillis);
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        this.timeInMillis = calendar.getTimeInMillis();
    }
    public int getMinute() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(this.timeInMillis);
        return calendar.get(Calendar.MINUTE);
    }
    public void setMinute(int minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(this.timeInMillis);
        calendar.set(Calendar.MINUTE, minute);
        this.timeInMillis = calendar.getTimeInMillis();
    }

    // day
    public void addDays(int numDays) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(this.timeInMillis);
        calendar.add(Calendar.DAY_OF_YEAR, numDays);
        this.timeInMillis = calendar.getTimeInMillis();
    }

    // week
    public Constant.EWeekDay getDayOfWeek() {
        // mon = 0 .. sun = 6
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(this.timeInMillis);
        return Constant.getDayOfWeek(calendar.get(Calendar.DAY_OF_WEEK));
    }
    public Boolean isDayOfWeekChecked(int dayOfWeek) { return this.bDayOfWeeks[dayOfWeek]; }
    public void setDayOfWeekChecked(int dayOfWeek, boolean bChecked) {
            this.bDayOfWeeks[dayOfWeek] = bChecked;
    }

    public boolean isRecurringChecked() { return bRecurring; }
    public void setRecurringChecked(boolean bRecurringChecked) { this.bRecurring = bRecurringChecked; }

    // holiday off
    public boolean isHolidayOffChecked() { return bHolidayOff; }
    public void setHolidayOffChecked(boolean bHolidayOff) { this.bHolidayOff = bHolidayOff; }

    // string format
    public String format(String pattern) {
        SimpleDateFormat simpleDateFormat = (SimpleDateFormat) SimpleDateFormat.getInstance();
        simpleDateFormat.applyPattern(pattern);
        return simpleDateFormat.format(new Date(this.timeInMillis));
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void scheduleNextAlarmDay() {
        this.timeInMillis = UWeek.computeDateFromToday(
                // compute earlaist day from today
                this.bDayOfWeeks,
                // for hour and minute
                this.timeInMillis,
                // if true, skip holiday
                this.bHolidayOff);
     }
}
