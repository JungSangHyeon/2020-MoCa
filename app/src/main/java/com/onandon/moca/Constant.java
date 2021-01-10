package com.onandon.moca;

import android.icu.util.Calendar;
import android.icu.util.ChineseCalendar;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.onandon.moca.model.MTime;

public class Constant {

// Common
    public final static int NotDefined = -1;

// DB
    public static int userId = 1; // TODO 나중에 서버까지 연동하면 제대로 줄 것
    public static String dbName = "UserDB";

// Alarm Callback
    public static final int AlarmWaitLimit = 1000*60*5; // ms, 5minute
    // Power
    public static int MaxPower = 100;
    public static int DefaultPower = 75;

    // Vibration Manager
    public static final int defaultVibrationPattern = 0;
    public static final long[] NoEffectVibrationPattern = {500, 2500, 500, 2500};
    public static final String[] VibrationNames= {
            "진동 패턴 1",
            "진동 패턴 2",
            "진동 패턴 3"
    };
    public static final long[][] VibrationTimings= {
            {300, 50,200,100,300, 50,200,100},
            {100, 100, 100, 100, 100, 100, 100, 100},
            {100,50,100,500,100,50,100,500}
    };
    public static final int[][] VibrationAmplitudes= { // max 255
            {255,150,255,100,255,150,255,100},
            {0, 255, 0, 255, 0, 255, 0, 255},
            {255,50,255,50,255,50,255,50}
    };
    // Flash Manager
    public static int FlashInterval = 100;
    // Screen Brightness
    public static int ScreenInterval = 1000;
    // Snooze
    public static int SnoozeInterval = 5; // min

// Alarm Setting
    // V Repeat
    public static class ReAlarm {
        public static final String[] interval = {"1", "3", "5", "8", "10"};
        public static final String[] count = {"1", "2", "3", "4", "5"};
    }

// Calendar
    public enum EWeekDay {
        eSun(1),
        eMon(2),
        eTue(3),
        eWed(4),
        eThu(5),
        eFri(6),
        eSat(7);

        private int calendarIndex;
        private EWeekDay(int calendarIndex) {
            this.calendarIndex = calendarIndex;
        }
        public int getCalendarIndex() {
            return this.calendarIndex;
        }
    }
    public static EWeekDay getDayOfWeek(int calendarIndex) {
        for (EWeekDay eWeekDay : EWeekDay.values()) {
            if (eWeekDay.getCalendarIndex() == calendarIndex) {
                return eWeekDay;
            }
        }
        return null;
    }

    public enum EMonth {
        eJan(0),
        eFeb(1),
        eMar(2),
        eApr(3),
        eMay(4),
        eJun(5),
        eJul(6),
        eAug(7),
        eSep(8),
        eOct(9),
        eNov(10),
        eDec(11);

        private final int calendarIndex;
        EMonth(int calendarIndex) {
            this.calendarIndex = calendarIndex;
        }
        public int getCalendarIndex() {
            return this.calendarIndex;
        }
    }
    public static EMonth getMonth(int calendarIndex) {
        for (EMonth eMonth : EMonth.values()) {
            if (eMonth.getCalendarIndex() == calendarIndex) {
                return eMonth;
            }
        }
        return null;
    }

    public enum EHolidayKorea {
        eNewYearsDay(1,1,"신정", false),
        // test
        eNewYearsDay3(1,5,"신정", false),
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
        private EHolidayKorea(int month, int day, String name, boolean bLunar) {
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
}
