package com.onandon.moca.technical;

import android.app.Activity;

import com.onandon.moca.Constant;
import com.onandon.moca.model.roomDatabase.entity.MAlarm;
import com.onandon.moca.ModeManager;
import com.onandon.moca.technical.device.TScreen;
import com.onandon.moca.technical.device.TFlash;
import com.onandon.moca.technical.device.TRingtone;
import com.onandon.moca.technical.device.TVibrator;

public class TAlarm {

    // Association
    private MAlarm mAlarm;
    private Activity activity;

    // Component
    private TDevices tDevices;

    // Constructor
    public TAlarm(Activity activity) {
        this.activity = activity;
    }
    public void setTargetMAlarm(MAlarm mAlarm) {
        this.mAlarm = mAlarm;
    }

    public void onStartCommand() {
        this.tDevices = new TDevices(this.activity, this.mAlarm);
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
        private MAlarm mAlarm;

        // Component
        private TRingtone tRingtone;
        private TVibrator tVibrator;
        private TFlash tFlash;
        private TScreen tScreen;

        // Constructor
        public TDevices(Activity activity, MAlarm mAlarm) {
            // Set Attribute
            this.bRunning = true;

            // Associate
            this.mAlarm = new ModeManager(activity).getMAlarmByMode(mAlarm);

            // Associate Attribute
            this.power = this.mAlarm.getPower();

            // Create Component
            this.tRingtone = new TRingtone(activity);
            this.tVibrator = new TVibrator(activity);
            this.tFlash = new TFlash(activity);
            this.tScreen = new TScreen(activity);

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
                    if (mAlarm.isFlashChecked() && flashWaitCount > Constant.FlashSwitchCount) {
                        flashWaitCount = 0;
                        tFlash.switchFlash();
                    }
                    if (mAlarm.isScreenChecked() && screenWaitCount > Constant.ScreenSwitchCount) {
                        screenWaitCount = 0;
                        tScreen.switchBrightness();
                    }
                }
            } catch (InterruptedException e) { e.printStackTrace(); }
        }

        public void onStart() {
            if (this.mAlarm.getRingtone().isChecked()) { this.tRingtone.start(this.mAlarm.getRingtone().getUri().toString()); }
            if (this.mAlarm.getVibration().isVibrationChecked()) { this.tVibrator.start(Constant.EVibrationPattern.values()[this.mAlarm.getVibration().getPattern()].getPattern(), 0); }
            if (this.mAlarm.isFlashChecked()) { this.tFlash.start(); }
            if (this.mAlarm.isScreenChecked()) { this.tScreen.start(); }
        }
        public void onStop() {
            if (this.mAlarm.getRingtone().isChecked()) { this.tRingtone.stop(); }
            if (this.mAlarm.getVibration().isVibrationChecked()) { this.tVibrator.stop(); }
            if (this.mAlarm.isFlashChecked()) { this.tFlash.stop(); }
            if (this.mAlarm.isScreenChecked()) { this.tScreen.stop(); }
        }
    }
}
