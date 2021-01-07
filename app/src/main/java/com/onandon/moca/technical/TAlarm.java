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
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void onCreate(MAlarm mAlarm) {
        this.mAlarm = mAlarm;
    }
    public void onDestroy() {
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void onStartCommand() {
        this.tDevices = new TDevices(this.activity, this.mAlarm);
        this.tDevices.onStart();
        this.tDevices.start();
        Log.d("TDevices", "start");
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void onStopCommand() {
        try {
            this.tDevices.setRunning(false);
            this.tDevices.join();
            this.tDevices.onStop();
            Log.d("TDevices", "stop");
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

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        public TDevices(Activity activity, MAlarm mAlarm) {
            this.bRunning = true;
            this.mAlarm = mAlarm;

            this.tRingtone = new TRingtone(activity);
            this.tVibrator = new TVibrator(activity);
            this.tFlash = new TFlash(activity);
            this.tScreen = new TScreen(activity);

            this.tRingtone.init(this.mAlarm.getPower());
            this.tVibrator.init(this.mAlarm.getPower());
        }
        private synchronized boolean isRunning() {
            return this.bRunning;
        }
        public synchronized void setRunning(boolean bRunning) {
            this.bRunning = bRunning;
        }

        @RequiresApi(api = Build.VERSION_CODES.M)
        public void run() {
            try {
                while (isRunning()) {
                    if (mAlarm.isFlashChecked()) {
                        tFlash.on();
                    }
                    if (mAlarm.isScreenChecked()) {
                        tScreen.on();
                    }
                    Thread.sleep(500);
                    if (mAlarm.isFlashChecked()) {
                        tFlash.off();
                    }
                    if (mAlarm.isScreenChecked()) {
                        tScreen.off();
                    }
                    Thread.sleep(500);
                    Log.d("TDevices", "run");
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        public void update() {
            if(mAlarm.getRingtone().isChecked()){ this.tRingtone.updatePower(this.mAlarm.getPower());}
            if(mAlarm.getVibration().isVibrationChecked()){this.tVibrator.updatePower(this.mAlarm.getPower());}
        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        public void onStart() {
            if (mAlarm.getRingtone().isChecked()) {
                tRingtone.start(mAlarm.getRingtone().getUri().toString());
            }
            if (mAlarm.getVibration().isVibrationChecked()) {
                tVibrator.start(Constant.VibrationTimings[mAlarm.getVibration().getPattern()],
                        Constant.VibrationAmplitudes[mAlarm.getVibration().getPattern()], 0);
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
