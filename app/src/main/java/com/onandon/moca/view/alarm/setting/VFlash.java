package com.onandon.moca.view.alarm.setting;

import android.view.View;
import android.widget.CompoundButton;

import com.onandon.moca.R;
import com.onandon.moca.model.MAlarm;
import com.onandon.moca.onAndOn.compoundView.OTitleInfoSwitchView;
import com.onandon.moca.onAndOn.oButton.oToggleButton.OVectorAnimationToggleButton;

public class VFlash implements CompoundButton.OnCheckedChangeListener {

    // Associations
    private final MAlarm currentAlarm;
    private OVectorAnimationToggleButton aSwitch;

    // Constructor
    public VFlash(View view, MAlarm currentAlarm) {
        this.currentAlarm = currentAlarm;

        OTitleInfoSwitchView itemTitleInfoSwitch = view.findViewById(R.id.alarm_setting_flash);

        aSwitch = itemTitleInfoSwitch.getOnOffButton();
        aSwitch.setOnCheckedChangeListener(this);
//        aSwitch.setChecked(this.currentAlarm.isFlashChecked());

        aSwitch.setCheckedWithoutAnimation(this.currentAlarm.isFlashChecked());
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        this.currentAlarm.setFlashChecked(isChecked);
    }

    public void setOnOffAndEnableDisable(boolean isChecked){
        if(isChecked){
            aSwitch.setChecked(this.currentAlarm.isFlashChecked());
        }else{
            aSwitch.setChecked(false);
        }
        aSwitch.setEnabled(isChecked);
    }
}
