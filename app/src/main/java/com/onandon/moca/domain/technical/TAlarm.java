package com.onandon.moca.domain.technical;

import android.app.Activity;
import android.util.Log;

import com.onandon.moca.domain.Constant;
import com.onandon.moca.domain.model.database.entity.MAlarmData;
import com.onandon.moca.domain.ModeManager;
import com.onandon.moca.onAndOnAsset.technical.device.TScreenBright;
import com.onandon.moca.onAndOnAsset.technical.device.TFlash;
import com.onandon.moca.onAndOnAsset.technical.device.TRingtone;
import com.onandon.moca.onAndOnAsset.technical.device.TVibrator;

public class TAlarm {

    // Association
    private MAlarmData mAlarmData;
    private Activity activity;

    // Component
    private TDevices tDevices;

    // Constructor
    public void onCreate(Activity activity) {
        this.activity = activity;
    }
    public void setTargetMAlarm(MAlarmData mAlarmData) {
        this.mAlarmData = mAlarmData;
    }

    public void onStartCommand() {
        this.tDevices = new TDevices(this.activity, this.mAlarmData);
        this.tDevices.onStart();
        this.tDevices.start();
    }
    public void onStopCommand() {
        try {
            this.tDevices.setRunning(false);
            this.tDevices.join();
            this.tDevices.onStop();
        } catch (InterruptedException e) { e.printStackTrace(); }
    }

    private class TDevices extends Thread {

        // Working Variable
        private boolean bRunning;
        private int power;

        // Associate
        private MAlarmData mAlarmData;

        // Component
        private TRingtone tRingtone;
        private TVibrator tVibrator;
        private TFlash tFlash;
        private TScreenBright tScreenBright;

        // Constructor
        public TDevices(Activity activity, MAlarmData mAlarmData) {
            // Set Attribute
            this.bRunning = true;

            // Associate
            this.mAlarmData = new ModeManager(activity).getMAlarmByMode(mAlarmData);

            // Associate Attribute
            this.power = this.mAlarmData.getPower();

            // Create Component
            this.tRingtone = new TRingtone(activity);
            this.tVibrator = new TVibrator(activity);
            this.tFlash = new TFlash(activity);
            this.tScreenBright = new TScreenBright(activity);

            // Initialize Component
            this.tRingtone.onCreate(this.power);
            this.tVibrator.onCreate(this.power);
        }
        private synchronized boolean isRunning() {
            return this.bRunning;
        }
        public synchronized void setRunning(boolean bRunning) {
            this.bRunning = bRunning;
        }

        public void run() {
            try {
                int flashWaitCount = 0, screenWaitCount = 0;
                while (this.isRunning()) {
                    Thread.sleep(Constant.waitTimePerCount/this.power);
                    flashWaitCount++;
                    screenWaitCount++;
                    if (mAlarmData.isFlashChecked() && flashWaitCount > Constant.FlashSwitchCount) {
                        flashWaitCount = 0;
                        tFlash.switchFlash();
                    }
                    if (mAlarmData.isScreenChecked() && screenWaitCount > Constant.ScreenSwitchCount) {
                        screenWaitCount = 0;
                        tScreenBright.switchBrightness();
                    }
                }
            } catch (InterruptedException e) { e.printStackTrace(); }
        }

        public void onStart() {
            if (this.mAlarmData.getRingtone().isChecked()) { this.tRingtone.start(this.mAlarmData.getRingtone().getUri().toString()); }
            if (this.mAlarmData.getVibration().isVibrationChecked()) { this.tVibrator.start(Constant.EVibrationPattern.values()[this.mAlarmData.getVibration().getPattern()].getPattern(), 0); }
            if (this.mAlarmData.isFlashChecked()) { this.tFlash.start(); }
            if (this.mAlarmData.isScreenChecked()) { this.tScreenBright.start(); }
        }
        public void onStop() {
            if (this.mAlarmData.getRingtone().isChecked()) {  this.tRingtone.stop(); }
            if (this.mAlarmData.getVibration().isVibrationChecked()) { this.tVibrator.stop(); }
            if (this.mAlarmData.isFlashChecked()) { this.tFlash.stop(); }
            if (this.mAlarmData.isScreenChecked()) { this.tScreenBright.stop(); }
        }
    }
}
