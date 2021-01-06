package com.onandon.moca.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.onandon.moca.activity.AAlarmCallback;
import com.onandon.moca.control.CAlarm;

import java.util.Locale;

public class RAlarm extends BroadcastReceiver {

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("RAlarm", intent.getAction());
        String toastText = String.format("RAlarm: " + intent.getAction());
        Toast.makeText(context, toastText, Toast.LENGTH_LONG).show();

        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            this.rescheduleAlarms(context, intent);
        } else if ("RAlarm.START".equals(intent.getAction())){
            this.startAlarm(context, intent);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void rescheduleAlarms(Context context, Intent intent) {
        Log.d("RAlarm", "rescheduleAlarms");
        String toastText = String.format("RAlarm: " + "rescheduleAlarms");
        Toast.makeText(context, toastText, Toast.LENGTH_LONG).show();

        CAlarm cAlarm = new CAlarm(context);
        cAlarm.onCreate(Locale.KOREA);
        cAlarm.scheduleAlarms();
    }
    private void startAlarm(Context context, Intent intent) {
        Log.d("RAlarm", "startAlarm");
        String toastText = String.format("RAlarm: " + "startAlarm");
        Toast.makeText(context, toastText, Toast.LENGTH_LONG).show();

        Bundle bundle = intent.getBundleExtra("bundle");
        Intent newIntent = new Intent(context, AAlarmCallback.class);
        newIntent.putExtra("bundle", bundle);
        newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(newIntent);
    }

}