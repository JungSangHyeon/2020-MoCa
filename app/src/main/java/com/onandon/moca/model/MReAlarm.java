package com.onandon.moca.model;

import com.onandon.moca.Constant;

import java.io.Serializable;
import java.util.Calendar;

public class MReAlarm implements Serializable, Cloneable {

    // Attribute
    private Calendar reAlarmTime;
    private boolean bChecked, isReAlarming;
    private int interval, count, nowReAlarmCount;

    @Override
    public MReAlarm clone() {
        try {
            return (MReAlarm) super.clone();
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }

    // Constructor
    public MReAlarm(){
        this.setChecked(false);
        this.setInterval(Integer.parseInt(Constant.ReAlarm.interval[0]));
        this.setCount(Integer.parseInt(Constant.ReAlarm.count[0]));
    }

    public void startReAlarm(){
        this.isReAlarming=true;
    }
    public void updateReAlarm() {
        if(this.nowReAlarmCount < this.count){
            this.nowReAlarmCount++;
            this.reAlarmTime = Calendar.getInstance();
            this.reAlarmTime.add(Calendar.SECOND, this.interval);
        }else{
            this.resetReAlarm();
        }
    }
    public void resetReAlarm() {
        this.setNowReAlarmCount(0);
        this.isReAlarming=false;
    }

    public void setNowReAlarmCount(int nowReAlarmCount) { this.nowReAlarmCount = nowReAlarmCount; }

    // Getter & Setter
    public boolean isChecked() {
        return bChecked;
    }
    public void setChecked(boolean bOn) {
        this.bChecked = bOn;
    }
    public int getInterval() {
        return interval;
    }
    public void setInterval(int interval) {
        this.interval = interval;
    }
    public int getCount() {
        return count;
    }
    public void setCount(int count) {
        this.count = count;
    }
    public boolean isReAlarmOn() {return this.bChecked; }
    public boolean isReAlarming() {return this.isReAlarming; }
    public long getReAlarmTime() {return this.reAlarmTime.getTimeInMillis(); }
}
