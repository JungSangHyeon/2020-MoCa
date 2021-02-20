package com.onandon.moca.control;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.onandon.moca.Constant;
import com.onandon.moca.R;
import com.onandon.moca.model.roomDatabase.entity.MAlarm;
import com.onandon.moca.receiver.RAlarm;
import com.onandon.moca.technical.DataAccessObject;

import java.io.Serializable;
import java.util.Locale;
import java.util.Vector;

public class CAlarm implements Serializable {

    // Attribute ?
    private Locale locale;

    // Associate
    private Vector<MAlarm> mAlarms;
    private final Context mainActivity;

    // Component
    private DataAccessObject dataAccessObject;

    // Working Variable
    private MAlarm currentAlarm;
    private int currentPosition;
    private boolean bCreate;

    // Constructor
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
    public void removeAlarm(int position) {
        this.mAlarms.remove(position);
        this.currentPosition = Constant.NotDefined;
        this.currentAlarm = null;
        this.store();
    }

    //////////////////////////////////////////////////////////////////

    public void scheduleAlarm() {
        MAlarm nextCloneAlarm = this.getNextCloneAlarm();
        if (nextCloneAlarm != null){
            AlarmManager alarmManager = (AlarmManager) this.mainActivity.getSystemService(Context.ALARM_SERVICE);
            Bundle bundle = new Bundle();
            bundle.putSerializable(MAlarm.class.getSimpleName(), nextCloneAlarm);
            Intent intentStartAlarm = new Intent(this.mainActivity, RAlarm.class);
            intentStartAlarm.setAction(this.mainActivity.getResources().getString(R.string.alarm_action));
            intentStartAlarm.putExtra(this.mainActivity.getResources().getString(R.string.alarm_bundle), bundle);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this.mainActivity, 0, intentStartAlarm, PendingIntent.FLAG_UPDATE_CURRENT);
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, nextCloneAlarm.getAlarmTime(), pendingIntent);
        }
    }

    public MAlarm findByKey(String key) {
        for (MAlarm mAlarm: this.mAlarms) {
            if (key.equals(mAlarm.schedulerNextAlarm().getKey())) { return mAlarm; }
        }
        return null;
    }
    public int getAlarmSize() { return this.mAlarms.size(); }
    public MAlarm getCurrentAlarm() { return this.currentAlarm; }
    public MAlarm getAlarm(int position) { return this.mAlarms.get(position); }
    public Vector<MAlarm> getMAlarms() { return mAlarms; }

    public MAlarm getNextCloneAlarm() {
        int nextAlarmIndex = this.getNextAlarmIndex();
        if (nextAlarmIndex != -1) { return this.mAlarms.get(nextAlarmIndex).schedulerNextAlarm(); }
        else{ return null; }
    }
    public int getNextAlarmIndex() {
        int index = -1;
        long minTime = Long.MAX_VALUE;
        for (int i = 0; i < this.mAlarms.size(); i++) {
            MAlarm cloneAlarm = this.mAlarms.get(i).schedulerNextAlarm();
            if (cloneAlarm.getAlarmTime() < minTime && cloneAlarm.isChecked()) {
                index = i;
                minTime = cloneAlarm.getAlarmTime();
            }
        }
        return index;
    }
}