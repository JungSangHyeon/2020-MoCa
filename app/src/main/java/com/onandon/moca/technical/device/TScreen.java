package com.onandon.moca.technical.device;

import android.app.Activity;
import android.view.Window;
import android.view.WindowManager;

import com.onandon.moca.Constant;

public class TScreen {

    // Attribute
    private float originalBrightness;

    // Associate
    private Activity activity;
    private Window window;
    private WindowManager.LayoutParams layoutParams;

    // Component
    private Thread flashThread;

    public TScreen(Activity activity) {
        this.activity=activity;
        this.window = activity.getWindow();
    }

    public void start() {
        this.layoutParams = this.window.getAttributes();
        this.originalBrightness = this.layoutParams.screenBrightness;
    }
    public void stop() {
        this.layoutParams.screenBrightness = this.originalBrightness;
        this.window.setAttributes(this.layoutParams);
    }

    public void on(){
        this.layoutParams.screenBrightness = WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_FULL;;
        this.window.setAttributes(this.layoutParams);
    }
    public void off(){
        this.layoutParams.screenBrightness = WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_OFF;
        this.window.setAttributes(this.layoutParams);
    }
}
