package com.onandon.moca.model;

import android.util.Log;

import androidx.annotation.NonNull;

import com.onandon.moca.Constant;

import java.io.Serializable;

public class MVibration implements Serializable, Cloneable {

    private boolean bChecked; // Vibration
    private int pattern;
    @NonNull
    @Override
    public MVibration clone() {
        try {
            return (MVibration) super.clone();
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }

    public MVibration() {
        this.setVibrationChecked(false);
        this.pattern = Constant.defaultVibrationPattern;
    }
    public boolean isVibrationChecked() {
        Log.println(Log.INFO,"MAlarm::isVibrationOn", Boolean.toString(bChecked));
        return bChecked;
    }
    public void setVibrationChecked(boolean bChecked) {
        Log.println(Log.INFO,"MAlarm::setVibrationOn", Boolean.toString(bChecked));
        this.bChecked = bChecked;
    }
    public int getPattern() {
        return pattern;
    }
    public void setPattern(int vibrationPatternID) {
        this.pattern = vibrationPatternID;
    }
}
