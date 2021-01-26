package com.onandon.moca.view.alarm.setting;

import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.material.switchmaterial.SwitchMaterial;
import com.onandon.moca.R;
import com.onandon.moca.model.MAlarm;
import com.onandon.moca.view.customView.OToggleButton;


public class VScreen implements Switch.OnCheckedChangeListener {

    // Association
    private MAlarm currentAlarm;

    // Constructor
    public VScreen(View view, MAlarm currentAlarm) {
        this.currentAlarm = currentAlarm;

        OToggleButton aSwitch = view.findViewById(R.id.alarm_setting_screen_on);
        aSwitch.setChecked(this.currentAlarm.isScreenChecked());
        aSwitch.setOnCheckedChangeListener(this);

        TextView screenTitle = view.findViewById(R.id.alarm_setting_screen_title);
        screenTitle.setOnClickListener((v)->{
            aSwitch.setChecked(!aSwitch.isChecked());
        });
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        this.currentAlarm.setScreenChecked(isChecked);
    }
}
