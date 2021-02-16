package com.onandon.moca.model;

import android.content.Context;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;

import com.onandon.moca.Constant;
import com.onandon.moca.technical.DataAccessObject;

import java.util.Vector;

public class ModeManager2 {

    // Associate
    private Context context;

    // Constructor
    public ModeManager2(Context context){
        this.context=context;
    }

    public MAlarm getMAlarmByMode(MAlarm mAlarm){
        switch (Constant.EAlarmMode.values()[mAlarm.getMode()]){
            case eNoSound: return this.getNoSoundMAlarm();
            case eSound: return this.getSoundMAlarm();
            case eCrazy: return this.getCrazyMAlarm();
            case eUserDefined: return mAlarm;
        }
        return null;
    }

    private MAlarm getCrazyMAlarm() {
        MAlarm mAlarm = this.getSoundMAlarm();
        mAlarm.setPowerLevel(Constant.EAlarmPower.eLevel3.ordinal());
        mAlarm.setScreenChecked(true);
        return mAlarm;
    }

    private MAlarm getSoundMAlarm() {
        MAlarm mAlarm = this.getNoSoundMAlarm();
        mAlarm.setPowerLevel(Constant.EAlarmPower.eLevel2.ordinal());
        MRingtone mRingtone = mAlarm.getRingtone();
        mRingtone.setChecked(true);
        return mAlarm;
    }

    private MAlarm getNoSoundMAlarm() {
        MAlarm mAlarm = new MAlarm();
        mAlarm.setPowerLevel(Constant.EAlarmPower.eLevel1.ordinal());
        MRingtone mRingtone = mAlarm.getRingtone();
        mRingtone.setChecked(false);
        Uri defaultRingtoneUri = RingtoneManager.getActualDefaultRingtoneUri(this.context.getApplicationContext(), RingtoneManager.TYPE_RINGTONE);
        Ringtone defaultRingtone = RingtoneManager.getRingtone(this.context, defaultRingtoneUri);
        mRingtone.setName(defaultRingtone.getTitle(this.context.getApplicationContext()));
        mRingtone.setUri(defaultRingtoneUri);
        MVibration mVibration = mAlarm.getVibration();
        mVibration.setVibrationChecked(true);
        mVibration.setPattern(Constant.EVibrationPattern.pattern_1.ordinal());
        mAlarm.setFlashChecked(true);
        mAlarm.setScreenChecked(false);
        return mAlarm;
    }
}
