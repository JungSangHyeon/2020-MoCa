package com.onandon.moca.technical;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.onandon.moca.R;
import com.onandon.moca.model.roomDatabase.entity.MAlarm;
import com.onandon.moca.receiver.RAlarm;

public class TAlarmManager {

    public static void scheduleAlarm(Context context, MAlarm target) {
        MAlarm nextCloneAlarm = target;
        if (nextCloneAlarm != null){
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            Bundle bundle = new Bundle();
            bundle.putSerializable(MAlarm.class.getSimpleName(), nextCloneAlarm);
            Intent intentStartAlarm = new Intent(context, RAlarm.class);
            intentStartAlarm.setAction(context.getResources().getString(R.string.alarm_action));
            intentStartAlarm.putExtra(context.getResources().getString(R.string.alarm_bundle), bundle);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intentStartAlarm, PendingIntent.FLAG_UPDATE_CURRENT);
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, nextCloneAlarm.getAlarmTime(), pendingIntent);
        }
    }

}
