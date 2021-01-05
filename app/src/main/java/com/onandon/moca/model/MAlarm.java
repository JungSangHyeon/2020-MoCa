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
            return cloned;
        } catch (CloneNotSupportedException e) {
            return new MAlarm(Constant.NotDefined);
        }
    }
    public static final String KEY_PATTERN = "EEEHHmm";
    public String getKey() {
        return this.getTime().format(KEY_PATTERN);
    }

    private boolean bScheduled;

    private int id; // Id -> remove
    private String name; // Name
    private boolean bChecked; // is On

    private MTime mTime; // Time
    private MRingtone mRingtone; // Sound
    private MVibration mVibration;
    private boolean bFlashChecked; // Flash
    private boolean bScreenChecked;
    private MReAlarm mReAlarm;

    public MAlarm(int index) {
        this.bScheduled = false;

        this.id = index;
        this.setName("alarm" + index);
        this.setChecked(true);

        // set current time as default time
        this.mTime = new MTime();

        this.mRingtone = new MRingtone();
        this.mVibration = new MVibration();
        this.setFlashChecked(false);
        this.setScreenChecked(false);
        this.mReAlarm = new MReAlarm();
    }

    public boolean isScehduled() {
        return bScheduled;
    }
    public void setScheduled(boolean bScheduled) {
        this.bScheduled = bScheduled;
    }

    public int getId() {
        return this.id;
    }
    public void setId(int id) {
        this.id = id;
    }
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

    @RequiresApi(api = Build.VERSION_CODES.N)
    public MAlarm schedulerNextAlarm() {
        MAlarm alarmScheduled = this.clone();
        alarmScheduled.getTime().scheduleNextAlarmDay();
        return alarmScheduled;
    }
}
