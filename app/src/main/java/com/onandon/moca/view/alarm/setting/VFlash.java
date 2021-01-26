package com.onandon.moca.view.alarm.setting;

import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.google.android.material.switchmaterial.SwitchMaterial;
import com.onandon.moca.R;
import com.onandon.moca.model.MAlarm;
import com.onandon.moca.view.customView.OToggleButton;

public class VFlash implements CompoundButton.OnCheckedChangeListener {

    // Associations
    private final MAlarm currentAlarm;

    // Constructor
    public VFlash(View view, MAlarm currentAlarm) {
        this.currentAlarm = currentAlarm;

        OToggleButton aSwitch = view.findViewById(R.id.alarm_setting_flash_on);
        aSwitch.setChecked(this.currentAlarm.isFlashChecked());
        aSwitch.setOnCheckedChangeListener(this);

        TextView flashTitle = view.findViewById(R.id.alarm_setting_flash_title);
        flashTitle.setOnClickListener((v)->{
            aSwitch.setChecked(!aSwitch.isChecked());
        });
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        this.currentAlarm.setFlashChecked(isChecked);
    }
}
