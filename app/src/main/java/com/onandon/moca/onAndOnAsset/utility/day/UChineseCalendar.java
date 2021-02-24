package com.onandon.moca.onAndOnAsset.utility.day;

import android.icu.util.ChineseCalendar;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@RequiresApi(api = Build.VERSION_CODES.N)
public class UChineseCalendar {

    public static String getDateByString(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }

    public static void convertLunarToSolar(Calendar calendar) {
        ChineseCalendar cc = new ChineseCalendar();
        cc.set(ChineseCalendar.EXTENDED_YEAR, calendar.get(Calendar.YEAR) + 2637);
        cc.set(ChineseCalendar.MONTH, calendar.get(Calendar.MONTH));
        cc.set(ChineseCalendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH));
        calendar.setTimeInMillis(cc.getTimeInMillis());
    }

    public static void convertSolarToLunar(Calendar calendar) {
        ChineseCalendar cc = new ChineseCalendar();
        cc.setTimeInMillis(calendar.getTimeInMillis());
        calendar.set(Calendar.YEAR, cc.get(ChineseCalendar.EXTENDED_YEAR) - 2637);
        calendar.set(Calendar.MONTH, cc.get(ChineseCalendar.MONTH));
        calendar.set(Calendar.DAY_OF_MONTH, cc.get(ChineseCalendar.DAY_OF_MONTH));
    }
}
