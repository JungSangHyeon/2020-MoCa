package com.onandon.moca.view.alarm.setting;

import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;

import com.onandon.moca.R;
import com.onandon.moca.model.MAlarm;
import com.onandon.moca.onAndOn.oButton.oToggleButton.OVectorAnimationToggleButton;
import com.onandon.moca.onAndOn.compoundView.OTitleInfoSwitchView;

public class VFlash implements CompoundButton.OnCheckedChangeListener {

    // Associations
    private final MAlarm currentAlarm;

    // Constructor
    public VFlash(View view, MAlarm currentAlarm) {
        this.currentAlarm = currentAlarm;

        OTitleInfoSwitchView itemTitleInfoSwitch = view.findViewById(R.id.alarm_setting_flash);

        OVectorAnimationToggleButton aSwitch = itemTitleInfoSwitch.getOnOffButton();
        aSwitch.setOnCheckedChangeListener(this);
//        aSwitch.setChecked(this.currentAlarm.isFlashChecked());

        aSwitch.setChecked(this.currentAlarm.isFlashChecked());
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        this.currentAlarm.setFlashChecked(isChecked);
    }
}
