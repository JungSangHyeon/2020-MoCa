package com.onandon.moca.onAndOnAsset.technical.device;

import android.content.Context;
import android.media.AudioDeviceInfo;
import android.media.AudioManager;

public class TEarphone {

    public static boolean isEarphoneConnected(Context context) {
        AudioManager mAudioManager = (AudioManager) context.getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
        AudioDeviceInfo[] devices = mAudioManager.getDevices(AudioManager.GET_DEVICES_OUTPUTS);
        boolean connected;
        for (AudioDeviceInfo device : devices) {
            connected = device.getType() == AudioDeviceInfo.TYPE_WIRED_HEADSET
                    || device.getType() == AudioDeviceInfo.TYPE_WIRED_HEADPHONES
                    || device.getType() == AudioDeviceInfo.TYPE_USB_HEADSET
                    || device.getType() == AudioDeviceInfo.TYPE_BLUETOOTH_SCO
                    || device.getType() == AudioDeviceInfo.TYPE_BLUETOOTH_A2DP
            ;
            if(connected){return true;}
        }
        return false;
    }

}
