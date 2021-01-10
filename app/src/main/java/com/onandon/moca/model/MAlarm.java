package com.onandon.moca.model;

import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.onandon.moca.Constant;

import java.io.Serializable;

public class MAlarm implements Serializable, Cloneable {

    @NonNull
    @Override
    public MAlarm clone() {
        try {
            MAlarm cloned = (MAlarm) super.clone();
            cloned.setTime(this.getTime().clone());
            cloned.setRingtone(this.getRingtone().clone());
            cloned.setVibration(this.getVibration().clone());
            cloned.setReAlarm(this.getReAlarm().clone());
            cloned.setmAlarmSnooze(this.getmAlarmSnooze().clone());
            return cloned;
        } catch (CloneNotSupportedException e) {
            return new MAlarm();
        }
    }
    public static final String KEY_PATTERN = "EEEHHmm";
    public String getKey() {
        return this.getTime().format(KEY_PATTERN);
    }

    private boolean bScheduled;

    private String name; // Name
    private boolean bChecked; // is On

    private MTime mTime; // Time
    private int power;
    private MRingtone mRingtone; // Sound
    private MVibration mVibration;
    private boolean bFlashChecked; // Flash
    private boolean bScreenChecked;
    private MReAlarm mReAlarm;
    private MSnooze mSnooze;

    public MAlarm() {
        this.bScheduled = false;

        this.setChecked(true);

        // set current time as default time
        this.mTime = new MTime();

        this.power = Constant.DefaultPower;
        this.mRingtone = new MRingtone();
        this.mVibration = new MVibration();
        this.setFlashChecked(false);
        this.setScreenChecked(false);
        this.mReAlarm = new MReAlarm();
        this.mSnooze = new MSnooze();
    }

//    public boolean isScehduled() {//?
//        return bScheduled;
//    }
//    public void setScheduled(boolean bScheduled) {
//        this.bScheduled = bScheduled;
//    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public boolean isChecked() {
        return bChecked;
    }
    public void setChecked(boolean bChecked) {
        this.bChecked = bChecked;
    }

    public MTime getTime() {
        return mTime;
    }
    public void setTime(MTime mTime) {
        this.mTime = mTime;
    }
    public int getPower() { return power; }
    public void setPower(int power) { this.power = power; }
    public MRingtone getRingtone() {
        return mRingtone;
    }
    public void setRingtone(MRingtone mRingtone) {
        this.mRingtone = mRingtone;
    }
    public MVibration getVibration() {
        return mVibration;
    }
    public void setVibration(MVibration mVibration) {
        this.mVibration = mVibration;
    }
    public boolean isFlashChecked() {
        return bFlashChecked;
    }
    public void setFlashChecked(boolean bFlashChecked) {
        this.bFlashChecked = bFlashChecked;
    }
    public boolean isScreenChecked() {
        return bScreenChecked;
    }
    public void setScreenChecked(boolean bScreenChecked) {
        this.bScreenChecked = bScreenChecked;
    }
    public MReAlarm getReAlarm() {
        return mReAlarm;
    }
    public void setReAlarm(MReAlarm mReAlarm) {
        this.mReAlarm = mReAlarm;
    }
    public MSnooze getmAlarmSnooze() { return mSnooze; }
    public void setmAlarmSnooze(MSnooze mSnooze) { this.mSnooze = mSnooze; }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public MAlarm schedulerNextAlarm() {
        MAlarm alarmScheduled = this.clone();
        alarmScheduled.getTime().scheduleNextAlarmDay();
        return alarmScheduled;
    }

    public long getAlarmTime() {
        if(this.mSnooze.isSnoozing()){return this.mSnooze.getSnoozeAlarmTime();}
        else if(this.mReAlarm.isReAlarming()){ return this.mReAlarm.getReAlarmTime(); }
        else{return this.getTime().getTimeInMillis();}
    }
}
