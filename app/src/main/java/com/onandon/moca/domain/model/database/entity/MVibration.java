package com.onandon.moca.domain.model.database.entity;

import androidx.annotation.NonNull;

import com.onandon.moca.domain.Constant;

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
        this.setVibrationChecked(true);
        this.pattern = Constant.EVibrationPattern.pattern_1.ordinal();
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
