package com.onandon.moca.view.alarm.setting;

import android.view.View;
import android.widget.CompoundButton;

import com.onandon.moca.R;
import com.onandon.moca.model.MAlarm;
import com.onandon.moca.model.MAlarmMode;
import com.onandon.moca.onAndOn.compoundView.OTitleInfoSwitchView;
import com.onandon.moca.onAndOn.oButton.oToggleButton.OVectorAnimationToggleButton;

public class VFlash implements CompoundButton.OnCheckedChangeListener {

    // Associations
    private final MAlarmMode currentAlarm;
    private OVectorAnimationToggleButton aSwitch;

    // Constructor
    public VFlash(View view, MAlarmMode currentAlarm) {
        this.currentAlarm = currentAlarm;

        OTitleInfoSwitchView itemTitleInfoSwitch = view.findViewById(R.id.alarm_setting_flash);

        this.aSwitch = itemTitleInfoSwitch.getOnOffButton();
        this.aSwitch.setOnCheckedChangeListener(this);
        this.aSwitch.setCheckedWithoutAnimation(this.currentAlarm.isFlashChecked());
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        this.currentAlarm.setFlashChecked(isChecked);
    }
}
