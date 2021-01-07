package com.onandon.moca.utility;

import android.icu.util.Calendar;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.onandon.moca.Constant;

public class UWeek {

//    @RequiresApi(api = Build.VERSION_CODES.N)
//    private static String isHolday(int dayCountFromToday, boolean bHolidayOff) {
//        String name = null;
//        if (bHolidayOff) {
//            Calendar calendar = Calendar.getInstance();
//            calendar.add(Calendar.DAY_OF_YEAR, dayCountFromToday);
//            name = UHolidays.isHoliday(calendar);
//        }
//        return name;
//    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static long computeDateFromToday(boolean[] bDayOfWeeks, long timeInMillis, boolean bHolidayOff) {
        Calendar tempTime = Calendar.getInstance();
        tempTime.setTimeInMillis(timeInMillis);

        Calendar targetTime = Calendar.getInstance();
        targetTime.set(Calendar.HOUR_OF_DAY, tempTime.get(Calendar.HOUR_OF_DAY));
        targetTime.set(Calendar.MINUTE, tempTime.get(Calendar.MINUTE));
        targetTime.set(Calendar.SECOND, 0);

        if (Calendar.getInstance().get(Calendar.MILLISECONDS_IN_DAY) > targetTime.get(Calendar.MILLISECONDS_IN_DAY)) {
            targetTime.add(Calendar.DATE, 1);
        }
        while(!bDayOfWeeks[targetTime.get(Calendar.DAY_OF_WEEK)-1] && (bHolidayOff || UHolidays.isHoliday(targetTime)==null)){
            targetTime.add(Calendar.DATE, 1);
        }

        return targetTime.getTimeInMillis();
        // target time
//        Calendar targetTime = Calendar.getInstance();
//        targetTime.setTimeInMillis(timeInMillis);
//        // current day time and day
//        Calendar currentTime = Calendar.getInstance();

        // translate Calendar.DAY_OF_WEEK to Constant.DAY_OF_WEEK
//        Constant.EWeekDay currentDayOfWeek =
//                Constant.getDayOfWeek(currentTime.get(Calendar.DAY_OF_WEEK));
//        Constant.EWeekDay currentDayOfWeek =
//                Constant.getDayOfWeek(currentTime.get(Calendar.DAY_OF_WEEK)-1);

        // find the earliest day from day of week
//        int dayCountFromToday = 0;
//        boolean found = false;

        // if today
//        if (bDayOfWeeks[currentDayOfWeek.ordinal()]) {
//            // if time is not passed
//            if (currentTime.get(Calendar.MILLISECONDS_IN_DAY) < targetTime.get(Calendar.MILLISECONDS_IN_DAY)) {
//                // today is the day
//                if (isHolday(dayCountFromToday, bHolidayOff)== null) {
//                    found = true;
//                }
//            }
//        }

        // re-search from tomorrow
//        for (dayCountFromToday = 1; !found; dayCountFromToday++) {
//            assert currentDayOfWeek != null;
//            int dayOfWeek = (currentDayOfWeek.ordinal() + dayCountFromToday) % 7;
//            // dayOfWeek is the checked
//            if (bDayOfWeeks[dayOfWeek]) {
//                if (isHolday(dayCountFromToday, bHolidayOff)==null) {
//                    found = true;
//                }
//            }
//        }
//        targetTime.add(Calendar.DAY_OF_YEAR, dayCountFromToday);

//        return targetTime.getTimeInMillis();
    }
}
