package com.onandon.moca.onAndOnAsset.technical.device;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;

import com.onandon.moca.domain.Constant;

/**
 * need permission : <uses-permission android:name="android.permission.VIBRATE" />
 * */
public class TVibrator {

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
    }
    public void onCreate(int power) {this.power=power; }

    public void start(int[][] pattern, int repeat) {
        long[] duration = this.getDuration(pattern);
        int[] amplitude = this.getAmplitude(pattern);
        this.running = true;
        this.vibrateThread = new Thread(){
            @Override
            public void run(){
                while (running){
                    try {
                        vibrate(duration, amplitude, repeat);
                        if(repeat!=-1){synchronized(this){this.wait();}}
                        else{ running = false;}
                    } catch (InterruptedException ignored) { }
                }
            }
        };
        this.vibrateThread.start();
    }

    public void vibrate(long[] timing, int[] amps, int repeat) {
        int[] powerApplyAmps = amps.clone();
        for (int i = 0; i < powerApplyAmps.length; i++) { powerApplyAmps[i] = powerApplyAmps[i] * this.power / 100; }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) this.vibrator.vibrate(VibrationEffect.createWaveform(timing, powerApplyAmps, repeat));
        else this.vibrator.vibrate(Constant.NoEffectVibrationPattern, repeat);
    }
    public void stop() {  this.running = false; this.vibrateThread.interrupt(); this.vibrator.cancel();}
    public void updatePower(int power) {this.power=power; this.vibrateThread.interrupt();} // stop -> update power -> play

    private long[] getDuration(int[][] pattern) {
        long[] duration = new long[pattern.length];
        for(int i=0; i< pattern.length; i++) duration[i] = pattern[i][0];
        return duration;
    }
    private int[] getAmplitude(int[][] pattern) {
        int[] amplitude = new int[pattern.length];
        for(int i=0; i< pattern.length; i++) amplitude[i] = pattern[i][1];
        return amplitude;
    }
}
