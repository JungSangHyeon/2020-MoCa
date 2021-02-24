package com.onandon.moca.domain.view.alarm.setting.comp;

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
import com.onandon.moca.onAndOnAsset.view.comp.widget.compoundView.OTitleInfoSwitchView;

public class VReAlarm implements OCustomViewCompLifeCycle, View.OnClickListener, Switch.OnCheckedChangeListener {

    // Attribute
    private String intervalUnitString, countUnitString;

    // Working Variable
    private int intervalIndex, countIndex;

    // Associate
    private MAlarmData mAlarmData;
        // View
        private TextView interval, intervalUnit, count, countUnit;
        private OVectorAnimationToggleButton aSwitch;

    /**
     * System Callback
     */
    @Override
    public void onCreate(Activity activity) {
        this.intervalUnitString = activity.getResources().getString(R.string.alarm_setting_realarm_intervalunit);
        this.countUnitString = activity.getResources().getString(R.string.alarm_setting_realarm_count_unit);
    }
    @Override
    public void associateAndInitView(View view){
        OTitleInfoSwitchView itemTitleInfoSwitch = view.findViewById(R.id.alarm_setting_realarm);
        this.aSwitch = itemTitleInfoSwitch.getOnOffButton();
        View reAlarmSettingLayout = itemTitleInfoSwitch.getSettingLayout();
        this.interval = reAlarmSettingLayout.findViewById(R.id.interval);
        this.intervalUnit = reAlarmSettingLayout.findViewById(R.id.interval_unit);
        this.count = reAlarmSettingLayout.findViewById(R.id.count);
        this.countUnit = reAlarmSettingLayout.findViewById(R.id.count_unit);

        this.aSwitch.setOnCheckedChangeListener(this);
        this.interval.setOnClickListener(this);
        this.intervalUnit.setOnClickListener(this);
        this.count.setOnClickListener(this);
        this.countUnit.setOnClickListener(this);
    }

    /**
     * Update
     */
    public void update(MAlarmData mAlarmData){
        this.mAlarmData = mAlarmData;
        this.intervalIndex = this.findIndex(Constant.ReAlarm.interval, this.mAlarmData.getReAlarm().getInterval());
        this.countIndex = this.findIndex(Constant.ReAlarm.count, this.mAlarmData.getReAlarm().getCount());
        this.aSwitch.setCheckedWithoutAnimation(this.mAlarmData.getReAlarm().isChecked());
    }
    private int findIndex(int[] intervals, int interval) {
        for(int i=0; i<intervals.length; i++){
            if(interval == intervals[i]){ return i; }
        }
        return -1;
    }

    /**
     * Callback
     */
    @Override // For "selectedNameView"
    public void onClick(View view) {
        if (view == this.interval || view == this.intervalUnit) { this.nextInterval(); }
        else if (view == this.count || view == this.countUnit) { this.nextCount(); }
        this.mAlarmData.getReAlarm().setChecked(true);
        this.aSwitch.setChecked(true);
    }
    private void nextInterval() {
        this.intervalIndex = (this.intervalIndex+1) % Constant.ReAlarm.interval.length;
        int newInterval = Constant.ReAlarm.interval[this.intervalIndex];
        this.mAlarmData.getReAlarm().setInterval(newInterval);
        this.interval.setText(Integer.toString(newInterval));
    }
    private void nextCount() {
        this.countIndex = (this.countIndex+1) % Constant.ReAlarm.count.length;
        int newCount = Constant.ReAlarm.count[this.countIndex];
        this.mAlarmData.getReAlarm().setCount(newCount);
        this.count.setText(Integer.toString(newCount));
    }
    @Override // For "switch"
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        this.mAlarmData.getReAlarm().setChecked(isChecked);

        this.interval.setText(isChecked? Integer.toString(this.mAlarmData.getReAlarm().getInterval()):"");
        this.intervalUnit.setText(isChecked? this.intervalUnitString:"");
        this.count.setText(isChecked? Integer.toString(this.mAlarmData.getReAlarm().getCount()):"");
        this.countUnit.setText(isChecked? this.countUnitString:"");
    }
}
