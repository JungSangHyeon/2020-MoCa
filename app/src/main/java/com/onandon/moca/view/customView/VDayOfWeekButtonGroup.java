package com.onandon.moca.view.customView;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.onandon.moca.Constant;
import com.onandon.moca.R;
import com.onandon.moca.model.MAlarm;

public class VDayOfWeekButtonGroup extends LinearLayout implements CompoundButton.OnCheckedChangeListener {

    public interface InterfaceSetAlarmDay { void setAlarmDay();}

    private MAlarm mAlarm;
    private InterfaceSetAlarmDay interfaceSetAlarmDay;

    private final VIndexToggleButton[] checkBoxes = new VIndexToggleButton[Constant.EWeekDay.values().length];
    private int numberOfDaysChecked;

    public VDayOfWeekButtonGroup(Context context, @Nullable AttributeSet attrs) { super(context, attrs); }

    public void init(InterfaceSetAlarmDay interfaceSetAlarmDay, MAlarm mAlarm){
        this.checkBoxes[Constant.EWeekDay.eSun.ordinal()] = this.findViewById(R.id.alarm_setting_weekdays_0);
        this.checkBoxes[Constant.EWeekDay.eMon.ordinal()] = this.findViewById(R.id.alarm_setting_weekdays_1);
        this.checkBoxes[Constant.EWeekDay.eTue.ordinal()] = this.findViewById(R.id.alarm_setting_weekdays_2);
        this.checkBoxes[Constant.EWeekDay.eWed.ordinal()] = this.findViewById(R.id.alarm_setting_weekdays_3);
        this.checkBoxes[Constant.EWeekDay.eThu.ordinal()] = this.findViewById(R.id.alarm_setting_weekdays_4);
        this.checkBoxes[Constant.EWeekDay.eFri.ordinal()] = this.findViewById(R.id.alarm_setting_weekdays_5);
        this.checkBoxes[Constant.EWeekDay.eSat.ordinal()] = this.findViewById(R.id.alarm_setting_weekdays_6);
        this.interfaceSetAlarmDay=interfaceSetAlarmDay;
        this.mAlarm=mAlarm;
        int index=0;
        for (VIndexToggleButton checkBox: this.checkBoxes) {
            checkBox.setChecked(this.mAlarm.getTime().isDayOfWeekChecked(index));
            checkBox.setOnCheckedChangeListener(this);
            checkBox.setIndex(index++);
        }
        this.numberOfDaysChecked = 1;
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
        VIndexToggleButton vIndexToggleButton = (VIndexToggleButton) compoundButton;
        // at least one be checked
        // if no checkbox is checked
        if (this.numberOfDaysChecked==1 && !isChecked) {
            // recheck current checkbox
            vIndexToggleButton.setChecked(true);
        } else {
            if (this.mAlarm.getTime().isDayOfWeekChecked(vIndexToggleButton.getIndex()) != isChecked) {
                this.numberOfDaysChecked = (isChecked) ? this.numberOfDaysChecked + 1 : this.numberOfDaysChecked - 1;
                this.mAlarm.getTime().setDayOfWeekChecked(vIndexToggleButton.getIndex(), isChecked);
                // recompute alarm schedule
                this.interfaceSetAlarmDay.setAlarmDay();
            }
        }
    }
}
