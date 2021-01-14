package com.onandon.moca.control;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.onandon.moca.Constant;
import com.onandon.moca.model.MAlarm;
import com.onandon.moca.receiver.RAlarm;
import com.onandon.moca.technical.DataAccessObject;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Locale;
import java.util.Vector;

public class CAlarm implements Serializable {

    private Locale locale;
    private Vector<MAlarm> mAlarms;
    private MAlarm currentAlarm;
    private int currentPosition;
    private boolean bCreate;

    private final Context mainActivity;
    private DataAccessObject dataAccessObject;

    public CAlarm(Context mainActivity) {
        this.mainActivity = mainActivity;
    }

    public void onCreate(Locale locale) {
        this.locale = locale;
        this.open();
        this.load();
    }
    public void onDestroy() {
        this.close();
    }

    //////////////////////////////////////////////////////////////////
    // file read & write
    private void open() {
        this.dataAccessObject = new DataAccessObject(this.mainActivity);
        this.mAlarms = null;
        this.currentAlarm = null;
        this.currentPosition = Constant.NotDefined;
        this.bCreate = false;
    }
    private void load() {
        this.mAlarms = this.dataAccessObject.read(Constant.DefaultFileName);
        if(this.mAlarms == null){
            this.mAlarms = new Vector<>();
        }
    }
    public void store() {
        this.dataAccessObject.save(Constant.DefaultFileName, this.mAlarms);
    }
    private void close() {
        this.dataAccessObject.close();

        this.dataAccessObject = null;
        this.mAlarms = null;

        this.currentAlarm = null;
        this.currentPosition = Constant.NotDefined;
        this.bCreate = false;
    }

    //////////////////////////////////////////////////////////////////
    public MAlarm findByKey(String key) {
        for (MAlarm mAlarm: this.mAlarms) {
            if (key.equals(mAlarm.getKey())) {
                return mAlarm;
            }
        }
        return null;
    }
    public int getAlarmSize() { return this.mAlarms.size(); }
    public MAlarm getAlarm() { return this.currentAlarm; }
    public MAlarm getAlarm(int postion) { return this.mAlarms.get(postion); }

    public void createAlarm() {
        this.currentPosition = this.mAlarms.size();
        this.currentAlarm = new MAlarm();
        this.bCreate = true;
    }
    public void editAlarm(int currentPosition) {
        this.currentPosition = currentPosition;
        this.currentAlarm = this.mAlarms.get(this.currentPosition).clone();
        this.bCreate = false;
    }
    public void saveAlarm() {
        if (!this.bCreate) this.mAlarms.remove(this.currentPosition);
        this.addWithTimeSort(this.currentAlarm);
    }

    private void addWithTimeSort(MAlarm currentAlarm) {
        if(this.mAlarms.size()==0){this.mAlarms.add(currentAlarm); return;}
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(currentAlarm.getTime().getTimeInMillis());
        int timeValue = calendar.get(Calendar.MINUTE) + calendar.get(Calendar.HOUR_OF_DAY)*60;
        for(int i=0; i<this.mAlarms.size(); i++){
            calendar.setTimeInMillis(this.mAlarms.get(i).getTime().getTimeInMillis());
            int tempTimeValue = calendar.get(Calendar.MINUTE) + calendar.get(Calendar.HOUR_OF_DAY)*60;
            if(timeValue < tempTimeValue){
                if(i==0){this.mAlarms.add(0, currentAlarm);}
                else{this.mAlarms.add(i-1, currentAlarm); }
                return;
            }
        }
        this.mAlarms.add(currentAlarm);
    }

    public void removeAlarm(int position) {
        this.mAlarms.remove(position);
        this.currentPosition = Constant.NotDefined;
        this.currentAlarm = null;
        this.store();
        // remove earlier scheduler
    }

    public void scheduleAlarm() {
        MAlarm nextCloneAlarm = null;
        long minTime = Long.MAX_VALUE;
        for(MAlarm mAlarm : this.mAlarms){
            MAlarm cloneAlarm = mAlarm.schedulerNextAlarm();
            if(cloneAlarm.getAlarmTime() < minTime){
                nextCloneAlarm = cloneAlarm;
                minTime = cloneAlarm.getAlarmTime();
            }
        }
        if (nextCloneAlarm != null && nextCloneAlarm.isChecked()){
            AlarmManager alarmManager = (AlarmManager) this.mainActivity.getSystemService(Context.ALARM_SERVICE);
            Bundle bundle = new Bundle();
            bundle.putSerializable(MAlarm.class.getSimpleName(), nextCloneAlarm);
            Intent intentStartAlarm = new Intent(this.mainActivity, RAlarm.class);
            intentStartAlarm.setAction("RAlarm.START");
            intentStartAlarm.putExtra("bundle", bundle);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this.mainActivity, 0, intentStartAlarm, PendingIntent.FLAG_UPDATE_CURRENT);
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, nextCloneAlarm.getAlarmTime(), pendingIntent);
        }
    }
}