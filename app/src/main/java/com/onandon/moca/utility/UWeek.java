package com.onandon.moca.utility;

import android.icu.util.Calendar;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.onandon.moca.Constant;

public class UWeek {

    public static long computeDateFromToday(boolean[] bDayOfWeeks, long timeInMillis, boolean bHolidayOff) {
        Calendar tempTime = Calendar.getInstance();
        tempTime.setTimeInMillis(timeInMillis);

        Calendar targetTime = Calendar.getInstance();
        targetTime.set(Calendar.HOUR_OF_DAY, tempTime.get(Calendar.HOUR_OF_DAY));
        targetTime.set(Calendar.MINUTE, tempTime.get(Calendar.MINUTE));
        targetTime.set(Calendar.SECOND, 0);
        targetTime.set(Calendar.MILLISECOND, 0);

        if (Calendar.getInstance().get(Calendar.MILLISECONDS_IN_DAY) > targetTime.get(Calendar.MILLISECONDS_IN_DAY)) {
            targetTime.add(Calendar.DATE, 1);
        }
        while(!bDayOfWeeks[targetTime.get(Calendar.DAY_OF_WEEK)-1] || (bHolidayOff && UHolidays.isHoliday(targetTime)!=null)){
            targetTime.add(Calendar.DATE, 1);
        }

        return targetTime.getTimeInMillis();
    }
}
