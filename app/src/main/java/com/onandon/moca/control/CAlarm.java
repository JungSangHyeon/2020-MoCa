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
            if (key.equals(mAlarm.schedulerNextAlarm().getKey())) {
                return mAlarm;
            }
        }
        return null;
    }
    public int getAlarmSize() { return this.mAlarms.size(); }
    public MAlarm getCurrentAlarm() { return this.currentAlarm; }
    public MAlarm getAlarm(int position) { return this.mAlarms.get(position); }
    public Vector<MAlarm> getMAlarms() { return mAlarms; }

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
        if (!this.bCreate) {
            this.mAlarms.remove(this.currentPosition);
            this.mAlarms.add(this.currentPosition, currentAlarm);
        }else{
            this.mAlarms.add(currentAlarm);
        }
    }

//    private void addWithTimeSort(MAlarm currentAlarm) {
//        if(this.mAlarms.size()==0){this.mAlarms.add(currentAlarm); return;}
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTimeInMillis(currentAlarm.getTime().getTimeInMillis());
//        int timeValue = calendar.get(Calendar.MINUTE) + calendar.get(Calendar.HOUR_OF_DAY)*60;
//        for(int i=0; i<this.mAlarms.size(); i++){
//            calendar.setTimeInMillis(this.mAlarms.get(i).getTime().getTimeInMillis());
//            int tempTimeValue = calendar.get(Calendar.MINUTE) + calendar.get(Calendar.HOUR_OF_DAY)*60;
//            if(timeValue < tempTimeValue){
//                if(i==0){this.mAlarms.add(0, currentAlarm);}
//                else{this.mAlarms.add(i, currentAlarm); }
//                return;
//            }
//        }
//        this.mAlarms.add(currentAlarm);
//    }

    public void removeAlarm(int position) {
        this.mAlarms.remove(position);
        this.currentPosition = Constant.NotDefined;
        this.currentAlarm = null;
        this.store();
        // remove earlier scheduler
    }

    public void scheduleAlarm() {
        MAlarm nextCloneAlarm = this.getNextCloneAlarm();
        if (nextCloneAlarm != null && nextCloneAlarm.isChecked()){
            AlarmManager alarmManager = (AlarmManager) this.mainActivity.getSystemService(Context.ALARM_SERVICE);
            Bundle bundle = new Bundle();
            bundle.putSerializable(MAlarm.class.getSimpleName(), nextCloneAlarm);
            Intent intentStartAlarm = new Intent(this.mainActivity, RAlarm.class);
            intentStartAlarm.setAction("RAlarm.START");
            intentStartAlarm.putExtra("bundle", bundle);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this.mainActivity, 0, intentStartAlarm, PendingIntent.FLAG_UPDATE_CURRENT);
//            alarmManager.setExact(AlarmManager.RTC_WAKEUP, nextCloneAlarm.getAlarmTime(), pendingIntent);
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, nextCloneAlarm.getAlarmTime(), pendingIntent);
        }
    }

    public MAlarm getNextCloneAlarm() {
        MAlarm nextCloneAlarm = null;
        long minTime = Long.MAX_VALUE;
        for(MAlarm mAlarm : this.mAlarms){
            MAlarm cloneAlarm = mAlarm.schedulerNextAlarm();
            if(cloneAlarm.getAlarmTime() < minTime && cloneAlarm.isChecked()){
                nextCloneAlarm = cloneAlarm;
                minTime = cloneAlarm.getAlarmTime();
            }
        }
        return nextCloneAlarm;
    }

    public int getNextAlarmIndex() {
        int index = -1;
        long minTime = Long.MAX_VALUE;
        for(int i=0; i<this.mAlarms.size(); i++){
            MAlarm cloneAlarm = this.mAlarms.get(i).schedulerNextAlarm();
            if(cloneAlarm.getAlarmTime() < minTime && cloneAlarm.isChecked()){
                index=i;
                minTime = cloneAlarm.getAlarmTime();
            }
        }
        return index;
    }

    public MAlarm getListAlarm(int position) {
        return this.createListAlarms().get(position);
    }

    private Vector<MAlarm> createListAlarms() {
        Vector<MAlarm> listAlarms = new Vector<>();
        listAlarms.addAll(this.mAlarms);
        int nextAlarmIndex = this.getNextAlarmIndex();
        if(nextAlarmIndex!=-1){
            MAlarm nextAlarm = listAlarms.get(nextAlarmIndex);
            listAlarms.remove(nextAlarmIndex);
            listAlarms.add(0, nextAlarm);
        }
        return listAlarms;
    }
}