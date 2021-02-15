package com.onandon.moca.model;

import androidx.annotation.NonNull;

import com.onandon.moca.Constant;

import java.io.Serializable;

public class MAlarmMode implements Serializable, Cloneable {

    @NonNull
    @Override
    public MAlarmMode clone() {
        try {
            MAlarmMode cloned = (MAlarmMode) super.clone();
            cloned.setRingtone(this.getRingtone().clone());
            cloned.setVibration(this.getVibration().clone());
            return cloned;
        } catch (CloneNotSupportedException e) { return new MAlarmMode(); }
    }

    // Attribute
    private int powerLevel;
    private boolean bAlarmPowerChecked;
    private MRingtone mRingtone; // Sound
    private MVibration mVibration;
    private boolean bFlashChecked; // Flash
    private boolean bScreenChecked;

    // Constructor
    public MAlarmMode() {

        // Set Attribute
        this.bAlarmPowerChecked = true;
        this.bFlashChecked = false;
        this.bScreenChecked = false;
        this.powerLevel = Constant.EAlarmPower.eLevel2.ordinal();

        // Create Component
        this.mRingtone = new MRingtone();
        this.mVibration = new MVibration();
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
}
