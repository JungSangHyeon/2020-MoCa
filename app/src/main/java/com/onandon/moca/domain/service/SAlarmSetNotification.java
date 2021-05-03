package com.onandon.moca.domain.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.opengl.Visibility;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.onandon.moca.R;
import com.onandon.moca.domain.activity.AMain;
import com.onandon.moca.domain.model.database.entity.MAlarmData;
import com.onandon.moca.domain.view.alarm.setting.comp.VTime;
import com.onandon.moca.onAndOnAsset.utility.UObjectAndByteArrayConverter;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class SAlarmSetNotification extends Service {

    // Associate
    private MAlarmData mAlarmData;

    @Override public IBinder onBind(Intent intent) { return null; }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        this.mAlarmData = (MAlarmData) UObjectAndByteArrayConverter.byteArrayToObject(
                (byte[]) intent.getSerializableExtra("NextAlarmInfo"));
        this.startForegroundService();
        return super.onStartCommand(intent, flags, startId);
    }

    private void startForegroundService() {
        Intent notificationIntent = new Intent(this, AMain.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

        NotificationCompat.Builder builder;
        if (Build.VERSION.SDK_INT >= 26) {
            String CHANNEL_ID = "moca_notification_channel";
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "MoCa Notification Channel",
                    NotificationManager.IMPORTANCE_LOW);
            channel.setShowBadge(false);
            channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            NotificationManager notificationManager = ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE));

//            notificationManager.deleteNotificationChannel(CHANNEL_ID);

            notificationManager.createNotificationChannel(channel);
            builder = new NotificationCompat.Builder(this, CHANNEL_ID);
        } else {
            builder = new NotificationCompat.Builder(this);
        }

        Calendar todayTime = Calendar.getInstance();
        Calendar alarmTime = Calendar.getInstance();
        alarmTime.setTimeInMillis(this.mAlarmData.schedulerNextAlarm().getAlarmTime());

        String day="";
        String year = alarmTime.get(Calendar.YEAR)+"년 ";
        String month = (alarmTime.get(Calendar.MONTH)+1)+"월 ";
        String date = alarmTime.get(Calendar.DAY_OF_MONTH)+"일 ";
        SimpleDateFormat simpleDateFormat = (SimpleDateFormat) SimpleDateFormat.getInstance();
        simpleDateFormat.applyPattern(VTime.DAY_OF_WEEK_PATTERN);
        String dayOfWeek =  simpleDateFormat.format(new Date(alarmTime.getTimeInMillis()))+" ";

        if(todayTime.get(Calendar.YEAR) != alarmTime.get(Calendar.YEAR)){
            day = year+month+date+dayOfWeek;
        } else if(todayTime.get(Calendar.MONTH) != alarmTime.get(Calendar.MONTH)) {
            day = month+date+dayOfWeek;
        }else if(todayTime.get(Calendar.DAY_OF_MONTH) != alarmTime.get(Calendar.DAY_OF_MONTH)) {
            day = date+dayOfWeek;
        }

        builder.setSmallIcon(R.drawable.ic_alarm)
                .setShowWhen(false)
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setContentTitle(day+this.mAlarmData.getTime().format(VTime.TIME_PATTERN))
                .setContentText(this.mAlarmData.getName()+ " 알람이 대기 중 입니다.")
                .setContentIntent(pendingIntent);
        startForeground(1, builder.build());
    }
}