package com.onandon.moca.technical;

import android.app.Activity;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.onandon.moca.Constant;
import com.onandon.moca.model.MAlarm;
import com.onandon.moca.model.MAlarmMode;
import com.onandon.moca.model.ModeManager;
import com.onandon.moca.technical.device.TScreen;
import com.onandon.moca.technical.device.TFlash;
import com.onandon.moca.technical.device.TRingtone;
import com.onandon.moca.technical.device.TVibrator;

import java.util.concurrent.Semaphore;

public class TAlarm {

    // Association
    private MAlarmMode mAlarm;
    private Activity activity;

    // Component
    private TDevices tDevices;

    // Constructor
    public TAlarm(Activity activity) {
        this.activity = activity;
    }
    public void onCreate(MAlarmMode mAlarm) {
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
        private MAlarmMode mAlarmMode;

        // Component
        private TRingtone tRingtone;
        private TVibrator tVibrator;
        private TFlash tFlash;
        private TScreen tScreen;

        // Constructor
        public TDevices(Activity activity, MAlarmMode mAlarm) {
            // Set Attribute
            this.bRunning = true;

            // Associate
            this.mAlarmMode = mAlarm;

            // Associate Attribute
            this.power = this.mAlarmMode.getPower();

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
                    if (mAlarmMode.isFlashChecked() && flashWaitCount > Constant.FlashSwitchCount) {
                        flashWaitCount = 0;
                        tFlash.switchFlash();
                    }
                    if (mAlarmMode.isScreenChecked() && screenWaitCount > Constant.ScreenSwitchCount) {
                        screenWaitCount = 0;
                        tScreen.switchBrightness();
                    }
                }
            } catch (InterruptedException e) { e.printStackTrace(); }
        }

        public void onStart() {
            if (this.mAlarmMode.getRingtone().isChecked()) { this.tRingtone.start(this.mAlarmMode.getRingtone().getUri().toString()); }
            if (this.mAlarmMode.getVibration().isVibrationChecked()) { this.tVibrator.start(Constant.EVibrationPattern.values()[this.mAlarmMode.getVibration().getPattern()].getPattern(), 0); }
            if (this.mAlarmMode.isFlashChecked()) { this.tFlash.start(); }
            if (this.mAlarmMode.isScreenChecked()) { this.tScreen.start(); }
        }
        public void onStop() {
            if (this.mAlarmMode.getRingtone().isChecked()) { this.tRingtone.stop(); }
            if (this.mAlarmMode.getVibration().isVibrationChecked()) { this.tVibrator.stop(); }
            if (this.mAlarmMode.isFlashChecked()) { this.tFlash.stop(); }
            if (this.mAlarmMode.isScreenChecked()) { this.tScreen.stop(); }
        }
    }
}
