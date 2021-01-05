package com.onandon.moca.utility;

import android.icu.util.Calendar;
import android.icu.util.ChineseCalendar;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.onandon.moca.Constant;

public class UHolidays {

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static String isHoliday(Calendar targetDay) {
        for  (Constant.EHolidayKorea eHoliday: Constant.EHolidayKorea.values()) {
            Calendar holiday = (Calendar) eHoliday.getCalendar();
            if (holiday.get(Calendar.DAY_OF_YEAR) == targetDay.get(Calendar.DAY_OF_YEAR)) {
                return eHoliday.getName();
            }
        }
        return null;
    }
}
