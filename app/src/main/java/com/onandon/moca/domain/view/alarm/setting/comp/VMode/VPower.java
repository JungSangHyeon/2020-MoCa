package com.onandon.moca.domain.view.alarm.setting.comp.VMode;

import android.app.Activity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.onandon.moca.domain.Constant;
import com.onandon.moca.R;
import com.onandon.moca.domain.model.database.entity.MAlarmData;
import com.onandon.moca.onAndOnAsset.view.comp.OCustomViewCompLifeCycle;
import com.onandon.moca.onAndOnAsset.view.comp.widget.compoundView.OTitleInfoView;
import com.onandon.moca.domain.technical.TAlarm;

public class VPower  implements OCustomViewCompLifeCycle, View.OnClickListener, View.OnTouchListener {

    // Associate
    private MAlarmData mAlarmData;
    private TextView name;

    // Working Variable
    private int selectedPowerLevel;

    private TAlarm tAlarm;

    /**
     * System Callback
     */
    @Override public void onCreate(Activity activity) { }
    @Override public void associateAndInitView(View view){
        OTitleInfoView itemTitleInfo = view.findViewById(R.id.alarm_setting_power);
        this.name = itemTitleInfo.getInfo();

        this.name.setOnClickListener(this);
        this.name.setOnTouchListener(this);
    }

    /**
     * Update
     */
    public void update(MAlarmData mAlarmData, TAlarm tAlarm) {
        this.mAlarmData = mAlarmData;
        this.tAlarm = tAlarm;

        this.selectedPowerLevel = this.mAlarmData.getPowerLevel();
        this.name.setText(Constant.EAlarmPower.values()[this.selectedPowerLevel].getLevelName());
    }

    /**
     * Callback
     */
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
        this.mAlarmData.setPowerLevel(this.selectedPowerLevel);
        this.name.setText(Constant.EAlarmPower.values()[this.selectedPowerLevel].getLevelName());
    }
}
