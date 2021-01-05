package com.onandon.moca.view.alarm.setting;

import android.icu.util.Calendar;
import android.os.Build;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatToggleButton;

import com.google.android.material.switchmaterial.SwitchMaterial;
import com.onandon.moca.Constant;
import com.onandon.moca.R;
import com.onandon.moca.model.MAlarm;
import com.onandon.moca.utility.UHolidays;
import com.onandon.moca.view.customView.MyToggleButton;

public class VTime implements
        TimePicker.OnTimeChangedListener, CompoundButton.OnCheckedChangeListener {

    // time
    public static final String TIME_PATTERN = "hh:mm a";
    private final TimePicker timePicker;

    // day
    public static final String DAY_PATTERN = "yyyy/MM/dd";
    private final TextView vDay;
    public static final String DAYOFWEEK_PATTERN = "(EEE)";
    private final TextView vDayOfWeek;

    // day of week
    private final AppCompatToggleButton[] checkBoxes
            = new AppCompatToggleButton[Constant.EWeekDay.values().length];

    // holidayoff
    private SwitchMaterial switchHolidayOff;

    private final MAlarm mAlarm;
    @RequiresApi(api = Build.VERSION_CODES.N)
    public VTime(View view, MAlarm mAlarm) {
        this.mAlarm = mAlarm;

        // time
        this.timePicker = view.findViewById(R.id.alarm_setting_time);
        // listener is affected by setHour/setMinute
        this.timePicker.setHour(this.mAlarm.getTime().getHourOfDay());
        this.timePicker.setMinute(this.mAlarm.getTime().getMinute());
        this.timePicker.setOnTimeChangedListener(this);

        // day
        this.vDay = view.findViewById(R.id.alarm_setting_day_date);
        this.vDayOfWeek = view.findViewById(R.id.alarm_setting_day_dayofweek);
        this.setAlarmDay();

//        this.checkboxx = view.findViewById(R.id.note);

         // day of week
        this.checkBoxes[Constant.EWeekDay.eSun.ordinal()] = view.findViewById(R.id.alarm_setting_weekdays_0);
        this.checkBoxes[Constant.EWeekDay.eMon.ordinal()] = view.findViewById(R.id.alarm_setting_weekdays_1);
        this.checkBoxes[Constant.EWeekDay.eTue.ordinal()] = view.findViewById(R.id.alarm_setting_weekdays_2);
        this.checkBoxes[Constant.EWeekDay.eWed.ordinal()] = view.findViewById(R.id.alarm_setting_weekdays_3);
        this.checkBoxes[Constant.EWeekDay.eThu.ordinal()] = view.findViewById(R.id.alarm_setting_weekdays_4);
        this.checkBoxes[Constant.EWeekDay.eFri.ordinal()] = view.findViewById(R.id.alarm_setting_weekdays_5);
        this.checkBoxes[Constant.EWeekDay.eSat.ordinal()] = view.findViewById(R.id.alarm_setting_weekdays_6);
        int index=0;
        for (AppCompatToggleButton checkBox: this.checkBoxes) {
            checkBox.setChecked(this.mAlarm.getTime().isDayOfWeekChecked(index));
            checkBox.setOnCheckedChangeListener(this);
            index++;
        }

        // holidayOff switch
        this.switchHolidayOff = view.findViewById(R.id.alarm_setting_holidayoff_on);
        this.switchHolidayOff.setChecked(this.mAlarm.getTime().isHolidayOffChecked());
        this.switchHolidayOff.setOnCheckedChangeListener(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void setAlarmDay() {
        MAlarm alarmScheduled = this.mAlarm.schedulerNextAlarm();
        // check holiday
        Calendar calendar = (Calendar) Calendar.getInstance();
        calendar.setTimeInMillis(alarmScheduled.getTime().getTimeInMillis());
        String hoiidayName = UHolidays.isHoliday(calendar);

        this.vDay.setText(alarmScheduled.getTime().format(DAY_PATTERN));
        this.vDayOfWeek.setText(alarmScheduled.getTime().format(DAYOFWEEK_PATTERN));
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
        this.mAlarm.getTime().setHourOfDay(hourOfDay);
        this.mAlarm.getTime().setMinute(minute);
        this.setAlarmDay();
     }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
        if (compoundButton == this.switchHolidayOff) {
            this.mAlarm.getTime().setHolidayOffChecked(isChecked);
        } else {
            this.checkDayOfWeek((MyToggleButton) compoundButton);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void checkDayOfWeek(MyToggleButton myToggleButton) {
        // at least one be checked
        boolean bChecked = false;
        for (AppCompatToggleButton checkBox: this.checkBoxes) {
            if (checkBox.isChecked()) {
                bChecked = true;
                break;
            }
        }
        // if no checkbox is checked
        if (!bChecked) {
            // recheck current checkbox
            myToggleButton.setChecked(true);
        }
        else {
//            int dayOfWeekChecked = Constant.NotDefined;
//            for (int dayOfWeek = 0; dayOfWeek< this.checkBoxes.length; dayOfWeek++) {
//                if (this.checkBoxes[dayOfWeek]  == myToggleButton) {
//                    dayOfWeekChecked = dayOfWeek;
//                    break;
//                }
//            }
            // find current checked index
            int dayOfWeekChecked = myToggleButton.getIndex();
            // save checkbox state
            this.mAlarm.getTime().setDayOfWeekChecked(dayOfWeekChecked, this.checkBoxes[dayOfWeekChecked].isChecked());
        }

        // recompute alarm schedule
        this.setAlarmDay();
    }
}
