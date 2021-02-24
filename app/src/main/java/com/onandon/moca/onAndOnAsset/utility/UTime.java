package com.onandon.moca.onAndOnAsset.utility;

import android.content.res.Resources;

import com.onandon.moca.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class UTime {

    public static String format(String format, long time){
        SimpleDateFormat simpleDateFormat = (SimpleDateFormat) SimpleDateFormat.getInstance();
        simpleDateFormat.applyPattern(format);
        return simpleDateFormat.format(new Date(time));
    }

    public static String getLeftTimeFromNow(long futureTime, Resources resources){
        long nowTime = Calendar.getInstance().getTimeInMillis();
        long leftTime = futureTime - nowTime + 60*1000;
        long hour = leftTime / (60*60*1000);
        leftTime = leftTime%(60*60*1000);
        long minute = leftTime / (60*1000);
        String result = "";
        if(hour!=0){result+=hour+resources.getString(R.string.hour)+" ";}
        result+=minute+resources.getString(R.string.minute)+" "+resources.getString(R.string.later);
        return result;
    }
}
