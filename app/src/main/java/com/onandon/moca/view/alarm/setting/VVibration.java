package com.onandon.moca.view.alarm.setting;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.google.android.material.switchmaterial.SwitchMaterial;
import com.onandon.moca.Constant;
import com.onandon.moca.R;
import com.onandon.moca.model.MAlarm;
import com.onandon.moca.technical.device.TVibrator;

public class VVibration implements Switch.OnCheckedChangeListener, View.OnClickListener {

    // Associate
    private View view;
    private MAlarm mAlarm;
    private TextView title, name;
    private SwitchMaterial aSwitch;

    // Working Variable
    private int selectedPattern;

    // Constructor
    public VVibration(View view, MAlarm mAlarm) {
        // Associate
        this.view = view;
        this.mAlarm = mAlarm;

        this.selectedPattern = this.mAlarm.getVibration().getPattern();

        this.name = view.findViewById(R.id.alarm_setting_vibration_name);
        this.name.setOnClickListener(this);

        this.aSwitch = view.findViewById(R.id.alarm_setting_vibration_on);
        this.aSwitch.setOnCheckedChangeListener(this);
        this.aSwitch.setChecked(this.mAlarm.getVibration().isVibrationChecked());

        this.title = view.findViewById(R.id.alarm_setting_vibration_title);
        this.title.setOnClickListener((v)->{
            this.aSwitch.setChecked(!this.aSwitch.isChecked());
        });
    }

    @Override
    public void onClick(View v) {
        this.selectedPattern = (this.selectedPattern+1 == Constant.VibrationNames.length)? 0:this.selectedPattern+1;
        this.mAlarm.getVibration().setPattern(this.selectedPattern);
        this.name.setText(Constant.VibrationNames[this.selectedPattern]);
        new TVibrator((Activity)this.view.getContext()).start(
                Constant.VibrationTimings[selectedPattern],
                Constant.VibrationAmplitudes[selectedPattern],
                -1);
    }
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        this.mAlarm.getVibration().setVibrationChecked(isChecked);
        this.name.setText(isChecked? Constant.VibrationNames[this.mAlarm.getVibration().getPattern()]:"");
    }
}

