package com.onandon.moca.view.alarm.setting;

import android.view.View;
import android.widget.SeekBar;

import com.onandon.moca.R;
import com.onandon.moca.model.MAlarm;
import com.onandon.moca.technical.TAlarm;

public class VPower implements SeekBar.OnSeekBarChangeListener {

    // Associate
    private MAlarm mAlarm;
    private SeekBar powerSeekBar;
    private TAlarm tAlarm;

    // Constructor
    public VPower(View view, MAlarm mAlarm, TAlarm tAlarm) {
        this.mAlarm = mAlarm;
        this.tAlarm = tAlarm;
        View powerItem = view.findViewById(R.id.alarm_setting_power);
        this.powerSeekBar = powerItem.findViewById(R.id.seekBar_power);

        // init
        this.powerSeekBar.setProgress(this.mAlarm.getPower());
        this.powerSeekBar.setOnSeekBarChangeListener(this);
    }

    @Override public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        this.mAlarm.setPower(progress); this.tAlarm.updatePower();}
    @Override public void onStartTrackingTouch(SeekBar seekBar) { this.tAlarm.onStartCommand(); }
    @Override public void onStopTrackingTouch(SeekBar seekBar) { this.tAlarm.onStopCommand(); }
}
