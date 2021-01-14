package com.onandon.moca.technical.device;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;

import java.io.IOException;

public class TRingtone {

    // Attribute
    private int originalMediaVolume, power;

    // Association
    private Activity activity;

    // Component
    private MediaPlayer mediaPlayer;

    // Constructor
    public TRingtone(Activity activity) { this.activity=activity; }
    public void onCreate(int power) {this.power=power;}
    public void updatePower(int power) { // for test power
        this.power=power;
        AudioManager mAudioManager = (AudioManager)this.activity.getSystemService(Context.AUDIO_SERVICE);
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
                mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)*this.power/100,
                AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
    }
    public void start(String soundUri) {
        // Get AudioManager
        AudioManager mAudioManager = (AudioManager) this.activity.getSystemService(Context.AUDIO_SERVICE);

        // Save Original Volume & Set Volume To Max
        this.originalMediaVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)*this.power/100, AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);

        // Start
        this.mediaPlayer = new MediaPlayer();
        this.mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            this.mediaPlayer.setDataSource(this.activity, Uri.parse(soundUri));
            this.mediaPlayer.prepare();
        } catch (IOException e) { e.printStackTrace(); }
        this.mediaPlayer.setLooping(true);
        this.mediaPlayer.start();
    }
    public void stop() {
        // Stop
        try {
            if (this.mediaPlayer.isPlaying()){
                this.mediaPlayer.stop();
                this.mediaPlayer.reset();
                this.mediaPlayer.release();
            }
        } catch (Exception e) {e.printStackTrace();}

        // Set Volume To Original Volume
        AudioManager mAudioManager = (AudioManager)this.activity.getSystemService(Context.AUDIO_SERVICE);
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, this.originalMediaVolume, AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
    }

//    // Attribute
//    private int originalMediaVolume;
//
//    // Association
//    private Activity activity;
//    // Component
//    private MediaPlayer mediaPlayer;
//
//    // Constructor
//    public TRingtone(Activity activity) {
//        this.activity=activity;
//    }
//
//    public void start(String soundUri) {
//        // Get AudioManager
//        AudioManager mAudioManager = (AudioManager) this.activity.getSystemService(Context.AUDIO_SERVICE);
//
//        // Save Original Volume & Set Volume To Max
//        this.originalMediaVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
//        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC), AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
//
//        // Start
//        this.mediaPlayer = new MediaPlayer();
//        this.mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
//        try {
//            this.mediaPlayer.setDataSource(this.activity, Uri.parse(soundUri));
//            this.mediaPlayer.prepare();
//        } catch (IOException e) { e.printStackTrace(); }
//        this.mediaPlayer.setLooping(true);
//        this.mediaPlayer.start();
//    }
//    public void stop() {
//        // Stop
//        try {
//            if (this.mediaPlayer.isPlaying()){
//                this.mediaPlayer.stop();
//                this.mediaPlayer.reset();
//                this.mediaPlayer.release();
//            }
//        } catch (Exception e) {e.printStackTrace();}
//
//        // Set Volume To Original Volume
//        AudioManager mAudioManager = (AudioManager)this.activity.getSystemService(Context.AUDIO_SERVICE);
//        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, this.originalMediaVolume, AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
//    }
}
