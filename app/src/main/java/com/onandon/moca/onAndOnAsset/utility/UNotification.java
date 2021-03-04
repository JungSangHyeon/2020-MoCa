package com.onandon.moca.onAndOnAsset.utility;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;

import androidx.core.app.NotificationCompat;

import com.onandon.moca.R;
import com.onandon.moca.domain.activity.AMain;
import com.onandon.moca.domain.service.SAlarmSetNotification;

public class UNotification {

//    private void updateService() {
//        if(this.isMyServiceRunning(SAlarmSetNotification.class) && this.getNextAlarm()==null){ // off
//            Intent intent = new Intent(this.getApplication().getApplicationContext(), SAlarmSetNotification.class);
//            this.getApplication().getApplicationContext().stopService(intent);
//        } else if(!this.isMyServiceRunning(SAlarmSetNotification.class) && this.getNextAlarm()!=null) { // on
//            Intent intent = new Intent(this.getApplication().getApplicationContext(), SAlarmSetNotification.class);
//            if (Build.VERSION.SDK_INT >= 26) {
//                this.getApplication().getApplicationContext().startForegroundService(intent);
//            } else {
//                this.getApplication().getApplicationContext().startService(intent);
//            }
//        }
//    }
//    private boolean isMyServiceRunning(Class<?> serviceClass) {
//        ActivityManager manager = (ActivityManager) this.getApplication().getSystemService(Context.ACTIVITY_SERVICE);
//        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
//            if (serviceClass.getName().equals(service.service.getClassName())) {
//                return true;
//            }
//        }
//        return false;
//    }

//    public class SAlarmSetNotification extends Service {
//
//        @Override public IBinder onBind(Intent intent) { return null; }
//        @Override public void onCreate() { super.onCreate(); this.startForegroundService(); }
//
//        void startForegroundService() {
//            Intent notificationIntent = new Intent(this, AMain.class);
//            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
//
//            NotificationCompat.Builder builder;
//            if (Build.VERSION.SDK_INT >= 26) {
//                String CHANNEL_ID = "moca_notification_channel";
//                NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "MoCa Notification Channel",
//                        NotificationManager.IMPORTANCE_LOW);
//                channel.setShowBadge(false);
//                channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
//                ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).createNotificationChannel(channel);
//                builder = new NotificationCompat.Builder(this, CHANNEL_ID);
//            } else {
//                builder = new NotificationCompat.Builder(this);
//            }
//            builder.setSmallIcon(R.drawable.ic_alarm)
//                    .setShowWhen(false)
//                    .setContentTitle("알람이 등록되어 있습니다.")
//                    .setContentIntent(pendingIntent);
//            startForeground(1, builder.build());
//        }
//    }

}
