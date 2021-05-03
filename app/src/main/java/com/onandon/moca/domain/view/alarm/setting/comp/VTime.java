package com.onandon.moca.domain.view.alarm.setting.comp;

import android.app.Activity;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import com.onandon.moca.R;
import com.onandon.moca.domain.model.database.entity.MAlarmData;
import com.onandon.moca.domain.view.alarm.setting.VAlarmSetting;
import com.onandon.moca.onAndOnAsset.view.comp.OCustomViewCompLifeCycle;
import com.onandon.moca.onAndOnAsset.view.comp.widget.compoundView.OTitleInfoSwitchView;
import com.onandon.moca.onAndOnAsset.view.comp.fragment.weekSelect.WeekListViewModel;
import com.onandon.moca.onAndOnAsset.view.comp.widget.oButton.oToggleButton.OVectorAnimationToggleButton;

public class VTime implements OCustomViewCompLifeCycle, TimePicker.OnTimeChangedListener, CompoundButton.OnCheckedChangeListener {

    // Constant
    public static final String TIME_PATTERN = "a hh:mm";
    public static final String DAY_PATTERN = "yyyy/MM/dd";
    public static final String DAY_OF_WEEK_PATTERN = "(EEE)";

    // Associate
    private MAlarmData mAlarmData;
        // View
        private TimePicker timePicker;
        private TextView vDay, vDayOfWeek;
        private OVectorAnimationToggleButton switchHolidayOff;
        // Model
        private WeekListViewModel weekListViewModel;

    /**
     * System Callback
     */
    @Override
    public void onCreate(Activity activity) {
    }
    public void createModel(VAlarmSetting vAlarmSetting) {
        this.weekListViewModel = new ViewModelProvider(vAlarmSetting).get(WeekListViewModel.class);
        this.weekListViewModel.getWeeks().observe(vAlarmSetting.getViewLifecycleOwner(), item -> { this.updateTime(); });
    }

    @Override
    public void associateAndInitView(View view){
        // Associate View
        this.timePicker = view.findViewById(R.id.alarm_setting_time);
        this.vDay = view.findViewById(R.id.alarm_setting_day_date);
        this.vDayOfWeek = view.findViewById(R.id.alarm_setting_day_dayofweek);
        this.switchHolidayOff = ((OTitleInfoSwitchView)view.findViewById(R.id.alarm_setting_holiday)).getOnOffButton();

        // Set View Callback
        this.timePicker.setOnTimeChangedListener(this);  // listener is affected by setHour/setMinute
        this.switchHolidayOff.setOnCheckedChangeListener(this);
    }

    /**
     * Update
     */
    private void updateTime() {
        if(this.mAlarmData !=null){
            MAlarmData alarmScheduled = this.mAlarmData.schedulerNextAlarm();
            this.vDay.setText(alarmScheduled.getTime().format(DAY_PATTERN));
            this.vDayOfWeek.setText(alarmScheduled.getTime().format(DAY_OF_WEEK_PATTERN));
        }
    }
    public void update(MAlarmData mAlarmData){
        this.mAlarmData = mAlarmData;

        this.timePicker.setOnTimeChangedListener(null);
        this.timePicker.setHour(this.mAlarmData.getTime().getHourOfDay());
        this.timePicker.setMinute(this.mAlarmData.getTime().getMinute());
        this.timePicker.setOnTimeChangedListener(this);
        this.timePicker.setVisibility(View.VISIBLE);

        this.weekListViewModel.setWeeks(this.mAlarmData.getTime().getBDayOfWeeks());

        this.switchHolidayOff.setCheckedWithoutAnimation(this.mAlarmData.getTime().isHolidayOffChecked());
    }

    /**
     * Callback
     */
    @Override
    public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
        this.mAlarmData.getTime().setHourOfDay(hourOfDay);
        this.mAlarmData.getTime().setMinute(minute);
        this.updateTime();
     }
    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
        this.mAlarmData.getTime().setHolidayOffChecked(isChecked);
        this.updateTime();
    }
}
