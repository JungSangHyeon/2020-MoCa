package com.onandon.moca.domain.model.database.entity;

import com.onandon.moca.domain.Constant;

import java.io.Serializable;
import java.util.Calendar;

public class MSnooze implements Serializable, Cloneable {

    @Override
    public MSnooze clone() {
        try { return (MSnooze) super.clone(); }
        catch (CloneNotSupportedException e) { return null; }
    }

    // Attribute
    private Calendar snoozeAlarmTime;
    private boolean isSnoozing;
    private int snoozeInterval;

    // Constructor
    public MSnooze(){
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
