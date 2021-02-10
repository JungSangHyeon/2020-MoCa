package com.onandon.moca.model;

import androidx.annotation.NonNull;

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
        } catch (CloneNotSupportedException e) { return new MAlarm(); }
    }

    // Attribute
    private boolean bScheduled; // ?
    private String name; // Name
    private boolean bChecked; // is On
    private MTime mTime; // Time
    private MReAlarm mReAlarm;
    private MSnooze mSnooze;
    private int powerLevel;
    private boolean bAlarmPowerChecked;
    private MRingtone mRingtone; // Sound
    private MVibration mVibration;
    private boolean bFlashChecked; // Flash
    private boolean bScreenChecked;

    // Constructor
    public MAlarm() {
        // Set Attribute
        this.bScheduled = false;
        this.bChecked = true;
        this.bAlarmPowerChecked = true;
        this.bFlashChecked = false;
        this.bScreenChecked = false;
        this.powerLevel = Constant.EAlarmPower.eLevel2.ordinal();

        // Create Component
        this.mTime = new MTime();  // set current time as default time
        this.mRingtone = new MRingtone();
        this.mVibration = new MVibration();
        this.mReAlarm = new MReAlarm();
        this.mSnooze = new MSnooze();
    }

    public MAlarm schedulerNextAlarm() {
        MAlarm alarmScheduled = this.clone();
        alarmScheduled.getTime().scheduleNextAlarmDay();
        return alarmScheduled;
    }
    public long getAlarmTime() {
//        Calendar c = Calendar.getInstance();
//        c.add(Calendar.SECOND, 5);
//        return c.getTimeInMillis();
        if(this.mSnooze.isSnoozing()){return this.mSnooze.getSnoozeAlarmTime();}
        else if(this.mReAlarm.isReAlarming()){ return this.mReAlarm.getReAlarmTime(); }
        else{return this.getTime().getTimeInMillis();}
    }
    public String getKey() { return Long.toString(this.getTime().getTimeInMillis()); }
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
    public boolean isbAlarmPowerChecked() { return bAlarmPowerChecked; }
    public void setbAlarmPowerChecked(boolean bAlarmPowerChecked) { this.bAlarmPowerChecked = bAlarmPowerChecked; }
    public int getPowerLevel() { return powerLevel; }
    public void setPowerLevel(int powerLevel) { this.powerLevel = powerLevel; }
    public int getPower() { return this.bAlarmPowerChecked?  Constant.EAlarmPower.values()[this.getPowerLevel()].getPower():0; }
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
}
