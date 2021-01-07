package com.onandon.moca.view.alarm.setting;

import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.material.switchmaterial.SwitchMaterial;
import com.onandon.moca.R;
import com.onandon.moca.model.MAlarm;


public class VScreen implements Switch.OnCheckedChangeListener {

    private final MAlarm currentAlarm;
    public VScreen(View view, MAlarm currentAlarm) {
        Log.println(Log.DEBUG, "VScreen", "");
        this.currentAlarm = currentAlarm;

        SwitchMaterial aSwitch = view.findViewById(R.id.alarm_setting_screen_on);
        aSwitch.setChecked(this.currentAlarm.isScreenChecked());
        aSwitch.setOnCheckedChangeListener(this);

        TextView screenTitle = view.findViewById(R.id.alarm_setting_screen_title);
        screenTitle.setOnClickListener((v)->{
            aSwitch.setChecked(!aSwitch.isChecked());
        });

        Log.d("VFlash::VFlash", Boolean.toString(aSwitch.isChecked()));
//        Log.d("cAlarmManager::getAlarm", "id:"+ this.currentAlarm.getId());
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        this.currentAlarm.setScreenChecked(isChecked);
    }
}
