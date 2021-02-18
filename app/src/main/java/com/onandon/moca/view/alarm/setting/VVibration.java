package com.onandon.moca.view.alarm.setting;

import android.app.Activity;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.onandon.moca.Constant;
import com.onandon.moca.R;
import com.onandon.moca.model.MAlarm;
import com.onandon.moca.onAndOn.oButton.oToggleButton.OVectorAnimationToggleButton;
import com.onandon.moca.technical.device.TVibrator;
import com.onandon.moca.onAndOn.compoundView.OTitleInfoSwitchView;

public class VVibration implements Switch.OnCheckedChangeListener, View.OnClickListener {

    // Associate
    private View view;
    private MAlarm mAlarm;
    private TextView name;
    private OVectorAnimationToggleButton aSwitch;

    // Working Variable
    private int selectedPattern;

    // Constructor
    public VVibration(View view, MAlarm mAlarm) {
        // Associate
        this.view = view;
        this.mAlarm = mAlarm;

        this.selectedPattern = this.mAlarm.getVibration().getPattern();

        OTitleInfoSwitchView itemTitleInfoSwitch = view.findViewById(R.id.alarm_setting_vibration);

        this.name = itemTitleInfoSwitch.getSettingLayout().findViewById(R.id.info);
        this.name.setOnClickListener(this);

        Constant.EVibrationPattern selectedVibrationPattern = Constant.EVibrationPattern.values()[this.selectedPattern];
        this.name.setText(this.mAlarm.getVibration().isVibrationChecked()? this.view.getResources().getString(selectedVibrationPattern.getNameId()):"");

        this.aSwitch = itemTitleInfoSwitch.getOnOffButton();
        this.aSwitch.setOnCheckedChangeListener(this);
        this.aSwitch.setCheckedWithoutAnimation(this.mAlarm.getVibration().isVibrationChecked());
    }

    @Override
    public void onClick(View v) {
        this.selectedPattern = (this.selectedPattern+1 == Constant.EVibrationPattern.values().length)? 0:this.selectedPattern+1;
        this.mAlarm.getVibration().setPattern(this.selectedPattern);
        Constant.EVibrationPattern selectedVibrationPattern = Constant.EVibrationPattern.values()[this.selectedPattern];
        this.name.setText(this.view.getResources().getString(selectedVibrationPattern.getNameId()));
        new TVibrator((Activity)this.view.getContext()).start(selectedVibrationPattern.getPattern(), -1);
    }
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        this.mAlarm.getVibration().setVibrationChecked(isChecked);
        String name = this.view.getResources().getString(Constant.EVibrationPattern.values()[this.mAlarm.getVibration().getPattern()].getNameId());
        this.name.setText(isChecked? name:"");
    }
}

