package com.onandon.moca.view.alarm.setting;

import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.google.android.material.switchmaterial.SwitchMaterial;
import com.onandon.moca.R;
import com.onandon.moca.model.MAlarm;

public class VFlash implements CompoundButton.OnCheckedChangeListener {

    // Associations
    private final MAlarm currentAlarm;
    public VFlash(View view, MAlarm currentAlarm) {
        Log.println(Log.DEBUG, "VFlash", "");
        this.currentAlarm = currentAlarm;

        SwitchMaterial aSwitch = view.findViewById(R.id.alarm_setting_flash_on);
        aSwitch.setChecked(this.currentAlarm.isFlashChecked());
        aSwitch.setOnCheckedChangeListener(this);

        TextView flashTitle = view.findViewById(R.id.alarm_setting_flash_title);
        flashTitle.setOnClickListener((v)->{
            aSwitch.setChecked(!aSwitch.isChecked());
        });

        Log.d("VFlash::VFlash", Boolean.toString(aSwitch.isChecked()));
//        Log.d("cAlarmManager::getAlarm", "id:"+ this.currentAlarm.getId());
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        this.currentAlarm.setFlashChecked(isChecked);
        Log.d("VFlash::onChecked", Boolean.toString(this.currentAlarm.isFlashChecked()));
    }
}
