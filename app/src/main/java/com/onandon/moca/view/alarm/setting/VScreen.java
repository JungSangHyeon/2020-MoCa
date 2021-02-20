package com.onandon.moca.view.alarm.setting;

import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.onandon.moca.R;
import com.onandon.moca.model.roomDatabase.entity.MAlarm;
import com.onandon.moca.onAndOn.oButton.oToggleButton.OVectorAnimationToggleButton;
import com.onandon.moca.onAndOn.compoundView.OTitleInfoSwitchView;


public class VScreen implements Switch.OnCheckedChangeListener {

    // Association
    private MAlarm currentAlarm;

    // Constructor
    public VScreen(View view, MAlarm currentAlarm) {
        this.currentAlarm = currentAlarm;

        OTitleInfoSwitchView itemTitleInfoSwitch = view.findViewById(R.id.alarm_setting_screen);

        OVectorAnimationToggleButton aSwitch = itemTitleInfoSwitch.getOnOffButton();
        aSwitch.setOnCheckedChangeListener(this);
        aSwitch.setCheckedWithoutAnimation(this.currentAlarm.isScreenChecked());
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        this.currentAlarm.setScreenChecked(isChecked);
    }
}
