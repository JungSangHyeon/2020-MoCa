package com.onandon.moca.model;
import com.onandon.moca.Constant;

import java.io.Serializable;

public class MReAlarm implements Serializable, Cloneable {
    // Repeat
    private boolean bChecked;
    private int interval;
    private int count;

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
}
