package com.onandon.moca.view.alarm.setting;

import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.material.switchmaterial.SwitchMaterial;
import com.onandon.moca.Constant;
import com.onandon.moca.R;
import com.onandon.moca.model.MAlarm;

public class VReAlarm implements View.OnClickListener, Switch.OnCheckedChangeListener {

    private int intervalIndex, countIndex;
    // Attribute
    private String intervalUnitString, countUnitString;
    // Components
    private TextView title;
    private TextView interval, intervalUnit;
    private TextView count, countUnit;
    private SwitchMaterial aSwitch;

    // Associations
    private View view;
    private MAlarm mAlarm;
    public VReAlarm(View view, MAlarm mAlarm) {
        this.view = view;
        this.mAlarm = mAlarm;

        this.intervalIndex = this.findIndex(Constant.ReAlarm.interval, this.mAlarm.getReAlarm().getInterval());
        this.countIndex = this.findIndex(Constant.ReAlarm.count, this.mAlarm.getReAlarm().getCount());

        this.intervalUnitString = this.view.getResources().getString(R.string.alarm_setting_realarm_intervalunit);
        this.countUnitString = this.view.getResources().getString(R.string.alarm_setting_realarm_count_unit);

        this.interval = view.findViewById(R.id.alarm_setting_realarm_interval);
        this.interval.setOnClickListener(this);

        this.intervalUnit = view.findViewById(R.id.alarm_setting_realarm_intervalunit);
        this.intervalUnit.setOnClickListener(this);

        this.count = view.findViewById(R.id.alarm_setting_realarm_count);
        this.count.setOnClickListener(this);

        this.countUnit = view.findViewById(R.id.alarm_setting_realarm_countunit);
        this.countUnit.setOnClickListener(this);

        this.aSwitch = view.findViewById(R.id.alarm_setting_realarm_on);
        this.aSwitch.setOnCheckedChangeListener(this);
        this.aSwitch.setChecked(this.mAlarm.getReAlarm().isChecked());

        this.title = view.findViewById(R.id.alarm_setting_realarm_title);
        this.title.setOnClickListener((v)->{
            this.aSwitch.setChecked(!this.aSwitch.isChecked());
        });
    }

    private int findIndex(String[] intervals, int interval) {
        for(int i=0; i<intervals.length; i++){
            if(interval == Integer.parseInt(intervals[i])){ return i; }
        }
        return -1;
    }

    // For "selectedNameView"
    @Override
    public void onClick(View view) {
        if (view == this.interval || view == this.intervalUnit) {
            this.nextInterval();
        } else if (view == this.count || view == this.countUnit) {
            this.nextCount();
        }
        this.mAlarm.getReAlarm().setChecked(true);
        this.aSwitch.setChecked(true);
    }
    private void nextInterval() {
        this.intervalIndex = (this.intervalIndex+1) % Constant.ReAlarm.interval.length;
        String newInterval = Constant.ReAlarm.interval[this.intervalIndex];
        this.mAlarm.getReAlarm().setInterval(Integer.parseInt(newInterval));
        this.interval.setText(newInterval);
    }
    private void nextCount() {
        this.countIndex = (this.countIndex+1) % Constant.ReAlarm.count.length;
        String newCount = Constant.ReAlarm.count[this.countIndex];
        this.mAlarm.getReAlarm().setCount(Integer.parseInt(newCount));
        this.count.setText(newCount);
    }

    // For "switch"
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        this.mAlarm.getReAlarm().setChecked(isChecked);

        this.interval.setText(isChecked? Integer.toString(this.mAlarm.getReAlarm().getInterval()):"");
        this.intervalUnit.setText(isChecked? this.intervalUnitString:"");
        this.count.setText(isChecked? Integer.toString(this.mAlarm.getReAlarm().getCount()):"");
        this.countUnit.setText(isChecked? this.countUnitString:"");
    }
}
