package com.onandon.moca.model;

import androidx.annotation.NonNull;

import com.onandon.moca.Constant;

import java.io.Serializable;

public class MVibration implements Serializable, Cloneable {

    @NonNull
    @Override
    public MVibration clone() {
        try { return (MVibration) super.clone(); }
        catch (CloneNotSupportedException e) { return null; }
    }

    // Attribute
    private boolean bChecked; // Vibration
    private int pattern;

    // Constructor
    public MVibration() {
        this.setVibrationChecked(false);
        this.pattern = Constant.defaultVibrationPattern;
    }
    public boolean isVibrationChecked() { return bChecked; }
    public void setVibrationChecked(boolean bChecked) { this.bChecked = bChecked; }
    public int getPattern() {
        return pattern;
    }
    public void setPattern(int vibrationPatternID) {
        this.pattern = vibrationPatternID;
    }
}
