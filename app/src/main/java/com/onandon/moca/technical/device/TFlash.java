package com.onandon.moca.technical.device;

import android.app.Activity;
import android.content.Context;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.onandon.moca.Constant;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class TFlash {

    /**
     *  need permission & feature
     *      <uses-permission android:name="android.permission.CAMERA" />
     *      <uses-permission android:name="android.permission.FLASHLIGHT" />
     *      <uses-feature android:name="android.hardware.camera.flash" />
     * */

    // Associate
    private CameraManager cameraManager;
    private Activity activity;

    // Constructor
    public TFlash(Activity activity) {
        this.activity = activity;
    }

    public void start() {
        this.cameraManager = (CameraManager) this.activity.getSystemService(Context.CAMERA_SERVICE);
    }
    public void stop() {
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void on(){
        try {
            this.cameraManager.setTorchMode(this.cameraManager.getCameraIdList()[0], true);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void off(){
        try {
            this.cameraManager.setTorchMode(this.cameraManager.getCameraIdList()[0], false);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }
}