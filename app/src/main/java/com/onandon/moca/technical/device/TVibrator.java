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
    // Component
    private Vibrator vibrator;

    // Constructor
    public TVibrator(Activity activity) {
        this.activity=activity;
        this.vibrator = (Vibrator) this.activity.getSystemService(Context.VIBRATOR_SERVICE);
    }

    public void start(long[] timing, int[] amp, int repeat) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            this.vibrator.vibrate(VibrationEffect.createWaveform(timing, amp, repeat));
        }else{
            this.vibrator.vibrate(Constant.NoEffectVibrationPattern, repeat);
        }
    }
    public void stop() { this.vibrator.cancel(); }
}
