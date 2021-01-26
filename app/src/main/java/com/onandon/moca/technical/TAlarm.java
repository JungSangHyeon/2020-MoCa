package com.onandon.moca.technical;

import android.app.Activity;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.onandon.moca.Constant;
import com.onandon.moca.model.MAlarm;
import com.onandon.moca.technical.device.TScreen;
import com.onandon.moca.technical.device.TFlash;
import com.onandon.moca.technical.device.TRingtone;
import com.onandon.moca.technical.device.TVibrator;

import java.util.concurrent.Semaphore;

public class TAlarm {

    private TDevices tDevices;
    // Association
    private Activity activity;
    private MAlarm mAlarm;

    // Constructor
    public TAlarm(Activity activity) {
        this.activity = activity;
    }
    public void onCreate(MAlarm mAlarm) {
        this.mAlarm = mAlarm;
    }
    public void onDestroy() { }

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
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public void updatePower(){
        this.tDevices.update();
    }

    private class TDevices extends Thread {
        private final Semaphore semaphore = new Semaphore(1);
        private boolean bRunning;

        private MAlarm mAlarm;

        private TRingtone tRingtone;
        private TVibrator tVibrator;
        private TFlash tFlash;
        private TScreen tScreen;

        public TDevices(Activity activity, MAlarm mAlarm) {
            this.bRunning = true;
            this.mAlarm = mAlarm;

            this.tRingtone = new TRingtone(activity);
            this.tVibrator = new TVibrator(activity);
            this.tFlash = new TFlash(activity);
            this.tScreen = new TScreen(activity);

            this.tRingtone.onCreate(this.mAlarm.getPower());
            this.tVibrator.onCreate(this.mAlarm.getPower());
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
                while (isRunning()) {
                    Thread.sleep(Constant.waitTimePerCount);

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
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        public void update() {
            if(mAlarm.getRingtone().isChecked()){ this.tRingtone.updatePower(this.mAlarm.getPower());}
            if(mAlarm.getVibration().isVibrationChecked()){this.tVibrator.updatePower(this.mAlarm.getPower());}
        }

        public void onStart() {
            if (mAlarm.getRingtone().isChecked()) {
                tRingtone.start(mAlarm.getRingtone().getUri().toString());
            }
            if (mAlarm.getVibration().isVibrationChecked()) {
                Constant.EVibrationPattern selectedVibrationPattern = Constant.EVibrationPattern.values()[mAlarm.getVibration().getPattern()];
                tVibrator.start(selectedVibrationPattern.getPattern(), 0);
            }
            if (mAlarm.isFlashChecked()) {
                tFlash.start();
            }
            if (mAlarm.isScreenChecked()) {
                tScreen.start();
            }
        }
        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        public void onStop() {
            if (this.mAlarm.getRingtone().isChecked()) {
                this.tRingtone.stop();
            }
            if (this.mAlarm.getVibration().isVibrationChecked()) {
                this.tVibrator.stop();
            }
            if (this.mAlarm.isFlashChecked()) {
                this.tFlash.stop();
            }
            if (this.mAlarm.isScreenChecked()) {
                this.tScreen.stop();
            }
        }
    }
}
