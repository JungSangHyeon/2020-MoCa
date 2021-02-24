package com.onandon.moca.domain.view.alarm.setting.comp.VMode;

import android.app.Activity;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.onandon.moca.R;
import com.onandon.moca.domain.model.database.entity.MAlarmData;
import com.onandon.moca.onAndOnAsset.view.comp.OCustomViewCompLifeCycle;
import com.onandon.moca.onAndOnAsset.view.comp.widget.oButton.oToggleButton.OVectorAnimationToggleButton;
import com.onandon.moca.onAndOnAsset.view.comp.widget.compoundView.OTitleInfoSwitchView;


public class VScreen implements OCustomViewCompLifeCycle, Switch.OnCheckedChangeListener {

    // Association
    private MAlarmData currentAlarm;
    private OVectorAnimationToggleButton aSwitch;

    /**
     * System Callback
     */
    @Override public void onCreate(Activity activity) { }
    @Override public void associateAndInitView(View view){
        OTitleInfoSwitchView itemTitleInfoSwitch = view.findViewById(R.id.alarm_setting_screen);
        this.aSwitch = itemTitleInfoSwitch.getOnOffButton();

        this.aSwitch.setOnCheckedChangeListener(this);
    }

    /**
     * Update
     */
    public void update(MAlarmData mAlarmData){
        this.currentAlarm = mAlarmData;
        this.aSwitch.setCheckedWithoutAnimation(this.currentAlarm.isScreenChecked());
    }

    /**
     * Callback
     */
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        this.currentAlarm.setScreenChecked(isChecked);
    }
}
