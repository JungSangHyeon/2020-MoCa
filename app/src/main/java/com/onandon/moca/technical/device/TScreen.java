package com.onandon.moca.technical.device;

import android.app.Activity;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.onandon.moca.Constant;

public class TScreen {

    // Attribute
    private boolean isOn;

    // Associate
    private Activity activity;
    private Window window;
    private WindowManager.LayoutParams layoutParams;

    // Constructor
    public TScreen(Activity activity) {
        this.activity=activity;
        this.window = activity.getWindow();
    }

    public void start() {
        this.layoutParams = this.window.getAttributes();
    }
    public void stop() {
        this.layoutParams.screenBrightness = WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_NONE;
        this.window.setAttributes(this.layoutParams);
    }

    public void switchBrightness() {
        if(this.isOn) this.off();
        else this.on();
    }
    public void on(){
        this.activity.runOnUiThread(()->{
            this.layoutParams.screenBrightness = WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_FULL;
            this.window.setAttributes(this.layoutParams);
            this.isOn = true;
        });
    }
    public void off(){
        this.activity.runOnUiThread(()->{
            this.layoutParams.screenBrightness = WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_OFF;
            this.window.setAttributes(this.layoutParams);
            this.isOn = false;
        });
    }
}
