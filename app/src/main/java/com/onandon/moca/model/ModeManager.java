package com.onandon.moca.model;

import android.content.Context;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;

import com.onandon.moca.Constant;
import com.onandon.moca.technical.DataAccessObject;

import java.util.Vector;

public class ModeManager {

    // Static Variable
    private static final String ModeFileName = "ModeFileName";

    // Associate
    private Context context;
    private Vector<MAlarmMode> mAlarmModes;

    // Component
    private DataAccessObject dataAccessObject;

    // Constructor
    public ModeManager(Context context){
        this.context=context;
        this.dataAccessObject = new DataAccessObject(context);
        this.load();
        if(this.mAlarmModes==null){ this.mAlarmModes = this.getDefaultMAlarmModes(); }
    }

    public void load() { this.mAlarmModes = this.dataAccessObject.read(ModeFileName); }
    public void save() { this.dataAccessObject.save(ModeFileName, this.mAlarmModes); }

    private Vector<MAlarmMode> getDefaultMAlarmModes() {
        Vector<MAlarmMode> result = new Vector<>();
        result.add(Constant.EAlarmMode.eNoSound.ordinal(), this.getNoSoundMode());
        result.add(Constant.EAlarmMode.eSound.ordinal(), this.getSoundMode());
        result.add(Constant.EAlarmMode.eCrazy.ordinal(), this.getCrazyMode());
        result.add(Constant.EAlarmMode.eUserDefined.ordinal(), this.getUserDefinedMode());
        return result;
    }
    private MAlarmMode getNoSoundMode() {
        MAlarmMode mode = new MAlarmMode();
        mode.setPowerLevel(Constant.EAlarmPower.eLevel1.ordinal());
        MRingtone mRingtone = mode.getRingtone();
        mRingtone.setChecked(false);
        Uri defaultRingtoneUri = RingtoneManager.getActualDefaultRingtoneUri(this.context.getApplicationContext(), RingtoneManager.TYPE_RINGTONE);
        Ringtone defaultRingtone = RingtoneManager.getRingtone(this.context, defaultRingtoneUri);
        mRingtone.setName(defaultRingtone.getTitle(this.context.getApplicationContext()));
        mRingtone.setUri(defaultRingtoneUri);
        MVibration mVibration = mode.getVibration();
        mVibration.setVibrationChecked(true);
        mVibration.setPattern(Constant.EVibrationPattern.pattern_1.ordinal());
        mode.setFlashChecked(true);
        mode.setScreenChecked(false);
        return mode;
    }
    private MAlarmMode getSoundMode() {
        MAlarmMode mode = this.getNoSoundMode();
        mode.setPowerLevel(Constant.EAlarmPower.eLevel2.ordinal());
        MRingtone mRingtone = mode.getRingtone();
        mRingtone.setChecked(true);
        return mode;
    }
    private MAlarmMode getCrazyMode() {
        MAlarmMode mode = this.getSoundMode();
        mode.setPowerLevel(Constant.EAlarmPower.eLevel3.ordinal());
        mode.setScreenChecked(true);
        return mode;
    }
    private MAlarmMode getUserDefinedMode() {
        return this.getSoundMode();
    }

    public MAlarmMode getMAlarmMode(int modeIndex){return this.mAlarmModes.get(modeIndex);}
    public void setMAlarmModes(Vector<MAlarmMode> mAlarmModes) {this.mAlarmModes = mAlarmModes;}
    public Vector<MAlarmMode> getCloneMAlarmModes(){
        Vector<MAlarmMode> result = new Vector<>();
        result.add(this.mAlarmModes.get(Constant.EAlarmMode.eNoSound.ordinal()).clone());
        result.add(this.mAlarmModes.get(Constant.EAlarmMode.eSound.ordinal()).clone());
        result.add(this.mAlarmModes.get(Constant.EAlarmMode.eCrazy.ordinal()).clone());
        result.add(this.mAlarmModes.get(Constant.EAlarmMode.eUserDefined.ordinal()).clone());
        return result;
    }
}
