package com.onandon.moca.technical.device;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;

import com.onandon.moca.Constant;

public class TVibrator {

    /**
     * need permission : <uses-permission android:name="android.permission.VIBRATE" />
     * */

    // Association
    private Activity activity;
    private boolean running;
    private int power;

    // Component
    private Vibrator vibrator;
    private Thread vibrateThread;

    // Constructor
    public TVibrator(Activity activity) {
        this.activity=activity;
        this.vibrator = (Vibrator) this.activity.getSystemService(Context.VIBRATOR_SERVICE);
        this.power=100;
    }
    public void init(int power) {this.power=power; }

    public void start(long[] timing, int[] amp, int repeat) {
        this.running = true;
        this.vibrateThread = new Thread(){
            @Override
            public void run(){
                while (running){
                    try {
                        vibrate(timing, amp, repeat);
                        if(repeat!=-1){synchronized(this){this.wait();}}
                        else{ running = false;}
                    } catch (InterruptedException e) { }
                }
            }
        };
        this.vibrateThread.start();
    }
    public void vibrate(long[] timing, int[] amps, int repeat) {
        int[] ampsCopy = amps.clone();
        for (int i = 0; i < ampsCopy.length; i++) { ampsCopy[i] = ampsCopy[i] * this.power / 100; }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) this.vibrator.vibrate(VibrationEffect.createWaveform(timing, ampsCopy, repeat));
        else this.vibrator.vibrate(Constant.NoEffectVibrationPattern, repeat);
    }
    public void stop() {  this.running = false; this.vibrateThread.interrupt(); this.vibrator.cancel();}
    public void updatePower(int power) {this.power=power; this.vibrateThread.interrupt();}

//    /**
//     * need permission : <uses-permission android:name="android.permission.VIBRATE" />
//     * */
//
//    // Association
//    private Activity activity;
//    // Component
//    private Vibrator vibrator;
//
//    // Constructor
//    public TVibrator(Activity activity) {
//        this.activity=activity;
//        this.vibrator = (Vibrator) this.activity.getSystemService(Context.VIBRATOR_SERVICE);
//    }
//
//    public void start(long[] timing, int[] amp, int repeat) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            this.vibrator.vibrate(VibrationEffect.createWaveform(timing, amp, repeat));
//        }else{
//            this.vibrator.vibrate(Constant.NoEffectVibrationPattern, repeat);
//        }
//    }
//    public void stop() { this.vibrator.cancel(); }
}
