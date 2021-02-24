package com.onandon.moca.onAndOnAsset.utility.day.holiday;

import android.icu.util.Calendar;

public class UHolidays {
    public static String isHoliday(Calendar targetDay) {
        for (EHolidayKorea eHoliday : EHolidayKorea.values()) {
            Calendar holiday = (Calendar) eHoliday.getCalendar();
            if (holiday.get(Calendar.DAY_OF_YEAR) == targetDay.get(Calendar.DAY_OF_YEAR)) {
                return eHoliday.getName();
            }
        }
        return null;
    }
}
