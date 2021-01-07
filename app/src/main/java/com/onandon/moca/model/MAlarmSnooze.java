package com.onandon.moca.model;

import com.onandon.moca.Constant;

import java.io.Serializable;
import java.util.Calendar;

public class MAlarmSnooze implements Serializable, Cloneable {

    @Override
    public MAlarmSnooze clone() {
        try {
            return (MAlarmSnooze) super.clone();
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }

    // Repeat
    private Calendar snoozeAlarmTime;
    private boolean isSnoozing;
    private int snoozeInterval;

    // Constructor
    public MAlarmSnooze(){
        this.snoozeInterval = Constant.SnoozeInterval;
        this.isSnoozing =false;
        this.snoozeAlarmTime =null;
    }

    // Method
    public void startSnooze() {
        this.isSnoozing = true;
        this.snoozeAlarmTime = Calendar.getInstance();
        this.snoozeAlarmTime.add(Calendar.MINUTE, this.snoozeInterval);
    }
    public void resetSnooze() { this.isSnoozing =false; }

    // Getter & Setter
    public Long getSnoozeAlarmTime() { return snoozeAlarmTime.getTimeInMillis(); }
    public boolean isSnoozing() { return isSnoozing; }
}
