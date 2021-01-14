package com.onandon.moca.utility;

import android.icu.util.Calendar;
import android.icu.util.ChineseCalendar;
import android.os.Build;

import androidx.annotation.RequiresApi;

public enum EHolidayKorea {
    eNewYearsDay(1,1,"신정", false),
    // test
    eNewYearsDay3(1,13,"asd", false),
    eSmailjeol(3,1,"삼일절", false),
    eChildrensDay(5,5,"어린이날", false),
    eMemorialDay(6,6,"현충일", false),
    eNationalLiberationDay(8,15,"광복절", false),
    eNationalFoundationDay(10,3,"개천절", false),
    eHangulnal(10,9,"한글날", false),
    eChristmas(12,25,"성탄절", false),
    eNewYearsDayLunar(1,1,"설날", true),
    eBuddhasDay(4,8,"석가탄신일", true),
    eChuseok(8,15,"추석", true);

    private int month;
    private int day;
    private String name;
    private boolean bLunar;
    EHolidayKorea(int month, int day, String name, boolean bLunar) {
        this.month = month;
        this.day = day;
        this.name = name;
        this.bLunar = bLunar;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public Calendar getCalendar() {
        Calendar calendar = Calendar.getInstance();
        if (bLunar) {
            ChineseCalendar chineseCalendar = new ChineseCalendar();
            chineseCalendar.set(ChineseCalendar.EXTENDED_YEAR, 2021 + 2637);
            chineseCalendar.set(ChineseCalendar.MONTH, this.month - 1);
            chineseCalendar.set(ChineseCalendar.DAY_OF_MONTH, this.day);
            calendar.setTimeInMillis(chineseCalendar.getTimeInMillis());
        } else {
            calendar.set(Calendar.MONTH, month-1);
            calendar.set(Calendar.DAY_OF_MONTH, day);
        }
        return calendar;
    }
    public String getName() {
        return name;
    }
    public boolean isLunar() {
        return bLunar;
    }
}
