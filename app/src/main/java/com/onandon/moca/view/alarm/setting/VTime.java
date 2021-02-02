package com.onandon.moca.view.alarm.setting;

import android.icu.util.Calendar;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.android.material.switchmaterial.SwitchMaterial;
import com.onandon.moca.R;
import com.onandon.moca.model.MAlarm;
import com.onandon.moca.view.customView.AlarmSettingItem;
import com.onandon.moca.view.customView.OToggleButton;
import com.onandon.moca.view.customView.VDayOfWeekButtonGroup;

public class VTime implements
        TimePicker.OnTimeChangedListener, CompoundButton.OnCheckedChangeListener, VDayOfWeekButtonGroup.InterfaceSetAlarmDay {

    // left time
    public static final String LEFT_TIME_PATTERN = "dd일 hh시 mm분 후";

    // time
    public static final String TIME_PATTERN = "hh:mm a";
    private final TimePicker timePicker;

    // day
    public static final String DAY_PATTERN = "yyyy/MM/dd";
    private final TextView vDay;
    public static final String DAYOFWEEK_PATTERN = "(EEE)";
    private final TextView vDayOfWeek;

    // day of week
    private VDayOfWeekButtonGroup vDayOfWeekButtonGroup;

    // holidayOff
    private OToggleButton switchHolidayOff;

    // Association
    private MAlarm mAlarm;

    // Constructor
    public VTime(View view, MAlarm mAlarm) {
        this.mAlarm = mAlarm;

        // time
        this.timePicker = view.findViewById(R.id.alarm_setting_time);
        this.timePicker.setHour(this.mAlarm.getTime().getHourOfDay());
        this.timePicker.setMinute(this.mAlarm.getTime().getMinute());
        this.timePicker.setOnTimeChangedListener(this);  // listener is affected by setHour/setMinute

        // day
        this.vDay = view.findViewById(R.id.alarm_setting_day_date);
        this.vDayOfWeek = view.findViewById(R.id.alarm_setting_day_dayofweek);
        this.setAlarmDay();

        // day of week
        this.vDayOfWeekButtonGroup = view.findViewById(R.id.alarm_setting_weekday);
        this.vDayOfWeekButtonGroup.onCreate(this, this.mAlarm);

        // holidayOff
        AlarmSettingItem alarmSettingItem = view.findViewById(R.id.alarm_setting_holiday);
        this.switchHolidayOff = alarmSettingItem.getOnOffButton();
        this.switchHolidayOff.setChecked(this.mAlarm.getTime().isHolidayOffChecked());
        this.switchHolidayOff.setOnCheckedChangeListener(this);
    }

    @Override
    public void setAlarmDay() {
        MAlarm alarmScheduled = this.mAlarm.schedulerNextAlarm();
        // check holiday
        Calendar calendar = (Calendar) Calendar.getInstance();
        calendar.setTimeInMillis(alarmScheduled.getTime().getTimeInMillis());

        this.vDay.setText(alarmScheduled.getTime().format(DAY_PATTERN));
        this.vDayOfWeek.setText(alarmScheduled.getTime().format(DAYOFWEEK_PATTERN));
    }

    @Override
    public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
        this.mAlarm.getTime().setHourOfDay(hourOfDay);
        this.mAlarm.getTime().setMinute(minute);
        this.setAlarmDay();
     }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
        if (compoundButton == this.switchHolidayOff) {
            this.mAlarm.getTime().setHolidayOffChecked(isChecked);
            this.setAlarmDay();
        }
    }
}
