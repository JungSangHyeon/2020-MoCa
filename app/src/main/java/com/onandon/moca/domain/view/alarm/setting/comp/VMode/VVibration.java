package com.onandon.moca.domain.view.alarm.setting.comp.VMode;

import android.app.Activity;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.onandon.moca.domain.Constant;
import com.onandon.moca.R;
import com.onandon.moca.domain.model.database.entity.MAlarmData;
import com.onandon.moca.onAndOnAsset.view.comp.OCustomViewCompLifeCycle;
import com.onandon.moca.onAndOnAsset.view.comp.widget.oButton.oToggleButton.OVectorAnimationToggleButton;
import com.onandon.moca.onAndOnAsset.technical.device.TVibrator;
import com.onandon.moca.onAndOnAsset.view.comp.widget.compoundView.OTitleInfoSwitchView;

public class VVibration implements OCustomViewCompLifeCycle, Switch.OnCheckedChangeListener, View.OnClickListener {

    // Associate
    private View view;
    private MAlarmData mAlarmData;
    private TextView name;
    private OVectorAnimationToggleButton aSwitch;

    // Working Variable
    private int selectedPattern;

    /**
     * System Callback
     */
    @Override public void onCreate(Activity activity) { }
    @Override public void associateAndInitView(View view){
        this.view = view;
        OTitleInfoSwitchView itemTitleInfoSwitch = view.findViewById(R.id.alarm_setting_vibration);
        this.name = itemTitleInfoSwitch.getSettingLayout().findViewById(R.id.info);
        this.aSwitch = itemTitleInfoSwitch.getOnOffButton();

        this.name.setOnClickListener(this);
        this.aSwitch.setOnCheckedChangeListener(this);
    }

    /**
     * Update
     */
    public void update(MAlarmData mAlarmData){
        this.mAlarmData = mAlarmData;
        this.selectedPattern = this.mAlarmData.getVibration().getPattern();
        Constant.EVibrationPattern selectedVibrationPattern = Constant.EVibrationPattern.values()[this.selectedPattern];
        this.name.setText(this.mAlarmData.getVibration().isVibrationChecked()? this.view.getResources().getString(selectedVibrationPattern.getNameId()):"");
        this.aSwitch.setCheckedWithoutAnimation(this.mAlarmData.getVibration().isVibrationChecked());
    }

    /**
     * Callback
     */
    @Override
    public void onClick(View v) {
        this.selectedPattern = (this.selectedPattern+1 == Constant.EVibrationPattern.values().length)? 0:this.selectedPattern+1;
        this.mAlarmData.getVibration().setPattern(this.selectedPattern);
        Constant.EVibrationPattern selectedVibrationPattern = Constant.EVibrationPattern.values()[this.selectedPattern];
        this.name.setText(this.view.getResources().getString(selectedVibrationPattern.getNameId()));
        new TVibrator((Activity)this.view.getContext()).start(selectedVibrationPattern.getPattern(), -1);
    }
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        this.mAlarmData.getVibration().setVibrationChecked(isChecked);
        String name = this.view.getResources().getString(Constant.EVibrationPattern.values()[this.mAlarmData.getVibration().getPattern()].getNameId());
        this.name.setText(isChecked? name:"");
    }
}

