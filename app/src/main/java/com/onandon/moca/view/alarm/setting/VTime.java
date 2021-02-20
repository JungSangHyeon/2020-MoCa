package com.onandon.moca.view.alarm.setting;

import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.fragment.app.FragmentActivity;

import com.onandon.moca.R;
import com.onandon.moca.model.roomDatabase.entity.MAlarm;
import com.onandon.moca.onAndOn.compoundView.OTitleInfoSwitchView;
import com.onandon.moca.onAndOn.compoundView.OWeekSelectView;
import com.onandon.moca.onAndOn.oButton.oToggleButton.OVectorAnimationToggleButton;

public class VTime implements TimePicker.OnTimeChangedListener, CompoundButton.OnCheckedChangeListener, OWeekSelectView.InterfaceSetAlarmDay {

    // Constant
    public static final String TIME_PATTERN = "hh:mm a";
    public static final String DAY_PATTERN = "yyyy/MM/dd";
    public static final String DAY_OF_WEEK_PATTERN = "(EEE)";

    // Associate
    private MAlarm mAlarm;
        // View
        private TimePicker timePicker;
        private TextView vDay, vDayOfWeek;
        private OWeekSelectView oWeekSelectView;
        private OVectorAnimationToggleButton switchHolidayOff;

    /**
     * System Callback
     */
    public void onCreate(FragmentActivity activity) { }
    public void onViewCreated(View view){
        // Associate View
        this.timePicker = view.findViewById(R.id.alarm_setting_time);
        this.vDay = view.findViewById(R.id.alarm_setting_day_date);
        this.vDayOfWeek = view.findViewById(R.id.alarm_setting_day_dayofweek);
        this.oWeekSelectView = view.findViewById(R.id.alarm_setting_weekday);
        this.oWeekSelectView.onViewCreated(this);
        this.switchHolidayOff = ((OTitleInfoSwitchView)view.findViewById(R.id.alarm_setting_holiday)).getOnOffButton();

        // Set View Callback
        this.timePicker.setOnTimeChangedListener(this);  // listener is affected by setHour/setMinute
        this.switchHolidayOff.setOnCheckedChangeListener(this);
    }

    /**
     * Update
     */
    public void update(MAlarm mAlarm){
        this.mAlarm = mAlarm;

        this.timePicker.setOnTimeChangedListener(null);
        this.timePicker.setHour(this.mAlarm.getTime().getHourOfDay());
        this.timePicker.setMinute(this.mAlarm.getTime().getMinute());
        this.timePicker.setOnTimeChangedListener(this);

        this.setAlarmDay();
        this.oWeekSelectView.update(this.mAlarm);
        this.switchHolidayOff.setCheckedWithoutAnimation(this.mAlarm.getTime().isHolidayOffChecked());
    }

    /**
     * Callback
     */
    @Override
    public void setAlarmDay() {
        MAlarm alarmScheduled = this.mAlarm.schedulerNextAlarm();
        this.vDay.setText(alarmScheduled.getTime().format(DAY_PATTERN));
        this.vDayOfWeek.setText(alarmScheduled.getTime().format(DAY_OF_WEEK_PATTERN));
    }
    @Override
    public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
        Log.d("TEST10", hourOfDay+", "+minute);
        this.mAlarm.getTime().setHourOfDay(hourOfDay);
        this.mAlarm.getTime().setMinute(minute);
        this.setAlarmDay();
     }
    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
        this.mAlarm.getTime().setHolidayOffChecked(isChecked);
        this.setAlarmDay();
    }
}
