package com.onandon.moca.view.alarm.setting;

import android.view.MotionEvent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.onandon.moca.Constant;
import com.onandon.moca.R;
import com.onandon.moca.model.MAlarm;
import com.onandon.moca.onAndOn.compoundView.OTitleInfoView;
import com.onandon.moca.technical.TAlarm;

public class VPower  implements Switch.OnCheckedChangeListener, View.OnClickListener, View.OnTouchListener {

    // Associate
    private MAlarm mAlarm;
    private TextView name;
//    private OVectorAnimationToggleButton aSwitch;

    // Working Variable
    private int selectedPowerLevel;

    private TAlarm tAlarm;

    // Constructor
    public VPower(View view, MAlarm mAlarm, TAlarm tAlarm) {
        this.mAlarm = mAlarm;
        this.tAlarm = tAlarm;

        this.selectedPowerLevel = this.mAlarm.getPowerLevel();

        OTitleInfoView itemTitleInfo = view.findViewById(R.id.alarm_setting_power);

        this.name = itemTitleInfo.getInfo();
        this.name.setOnClickListener(this);
        this.name.setOnTouchListener(this);
        this.name.setText(Constant.EAlarmPower.values()[this.selectedPowerLevel].getLevelName());

//        this.aSwitch = itemTitleInfo.getOnOffButton();
//        this.aSwitch.setOnCheckedChangeListener(this);
//        this.aSwitch.setCheckedWithoutAnimation(this.mAlarm.isbAlarmPowerChecked());
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                this.tAlarm.onStartCommand(); break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                this.tAlarm.onStopCommand(); break;
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        this.selectedPowerLevel = (this.selectedPowerLevel+1 == Constant.EAlarmPower.values().length)? 0:this.selectedPowerLevel+1;
        this.mAlarm.setPowerLevel(this.selectedPowerLevel);
        this.name.setText(Constant.EAlarmPower.values()[this.selectedPowerLevel].getLevelName());
//        this.tAlarm.updatePower();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//        this.mAlarm.setbAlarmPowerChecked(isChecked);
//        this.name.setText(isChecked? Constant.EAlarmPower.values()[this.selectedPowerLevel].getLevelName():"");
    }
}
