package com.onandon.moca.view.alarm.setting;

import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.google.android.material.switchmaterial.SwitchMaterial;
import com.onandon.moca.R;
import com.onandon.moca.model.MAlarm;
import com.onandon.moca.view.customView.AlarmSettingItem;
import com.onandon.moca.view.customView.OToggleButton;

public class VFlash implements CompoundButton.OnCheckedChangeListener {

    // Associations
    private final MAlarm currentAlarm;

    // Constructor
    public VFlash(View view, MAlarm currentAlarm) {
        this.currentAlarm = currentAlarm;

        AlarmSettingItem alarmSettingItem = view.findViewById(R.id.alarm_setting_flash);

        OToggleButton aSwitch = alarmSettingItem.getOnOffButton();
        aSwitch.setChecked(this.currentAlarm.isFlashChecked());
        aSwitch.setOnCheckedChangeListener(this);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        this.currentAlarm.setFlashChecked(isChecked);
    }
}
