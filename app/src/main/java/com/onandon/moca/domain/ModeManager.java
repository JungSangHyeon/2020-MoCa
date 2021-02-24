package com.onandon.moca.domain;

import android.content.Context;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;

import com.onandon.moca.domain.model.database.entity.MAlarmData;
import com.onandon.moca.domain.model.database.entity.MRingtone;
import com.onandon.moca.domain.model.database.entity.MVibration;

public class ModeManager {

    // Associate
    private Context context;

    // Constructor
    public ModeManager(Context context){
        this.context=context;
    }

    public MAlarmData getMAlarmByMode(MAlarmData mAlarmData){
        switch (Constant.EAlarmMode.values()[mAlarmData.getMode()]){
            case eNoSound: return this.getNoSoundMAlarm();
            case eSound: return this.getSoundMAlarm();
            case eCrazy: return this.getCrazyMAlarm();
            case eUserDefined: return mAlarmData;
        }
        return null;
    }

    private MAlarmData getCrazyMAlarm() {
        MAlarmData mAlarmData = this.getSoundMAlarm();
        mAlarmData.setPowerLevel(Constant.EAlarmPower.eLevel3.ordinal());
        mAlarmData.setScreenChecked(true);
        return mAlarmData;
    }

    private MAlarmData getSoundMAlarm() {
        MAlarmData mAlarmData = this.getNoSoundMAlarm();
        mAlarmData.setPowerLevel(Constant.EAlarmPower.eLevel2.ordinal());
        MRingtone mRingtone = mAlarmData.getRingtone();
        mRingtone.setChecked(true);
        return mAlarmData;
    }

    private MAlarmData getNoSoundMAlarm() {
        MAlarmData mAlarmData = new MAlarmData();
        mAlarmData.setPowerLevel(Constant.EAlarmPower.eLevel1.ordinal());
        MRingtone mRingtone = mAlarmData.getRingtone();
        mRingtone.setChecked(false);
        Uri defaultRingtoneUri = RingtoneManager.getActualDefaultRingtoneUri(this.context.getApplicationContext(), RingtoneManager.TYPE_RINGTONE);
        Ringtone defaultRingtone = RingtoneManager.getRingtone(this.context, defaultRingtoneUri);
        mRingtone.setName(defaultRingtone.getTitle(this.context.getApplicationContext()));
        mRingtone.setUri(defaultRingtoneUri);
        MVibration mVibration = mAlarmData.getVibration();
        mVibration.setVibrationChecked(true);
        mVibration.setPattern(Constant.EVibrationPattern.pattern_1.ordinal());
        mAlarmData.setFlashChecked(true);
        mAlarmData.setScreenChecked(false);
        return mAlarmData;
    }
}
