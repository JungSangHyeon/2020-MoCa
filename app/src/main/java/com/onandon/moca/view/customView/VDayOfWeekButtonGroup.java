package com.onandon.moca.view.customView;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.onandon.moca.Constant;
import com.onandon.moca.R;
import com.onandon.moca.model.MAlarm;

public class VDayOfWeekButtonGroup extends LinearLayout implements CompoundButton.OnCheckedChangeListener {

    public interface InterfaceSetAlarmDay { void setAlarmDay();}

    // Working Variable
    private int numberOfDaysChecked;

    // Associate
    private MAlarm mAlarm;
    private InterfaceSetAlarmDay interfaceSetAlarmDay;

    // Component
    private VIndexToggleButton[] checkBoxes;

    // Constructor
    public VDayOfWeekButtonGroup(Context context, @Nullable AttributeSet attrs) { super(context, attrs); }

    public void init(InterfaceSetAlarmDay interfaceSetAlarmDay, MAlarm mAlarm){
        // Create Component
        this.checkBoxes = new VIndexToggleButton[Constant.EWeekDay.values().length];

        // Associate
        this.interfaceSetAlarmDay=interfaceSetAlarmDay;
        this.mAlarm=mAlarm;
        this.checkBoxes[Constant.EWeekDay.eSun.ordinal()] = this.findViewById(R.id.alarm_setting_weekdays_0);
        this.checkBoxes[Constant.EWeekDay.eMon.ordinal()] = this.findViewById(R.id.alarm_setting_weekdays_1);
        this.checkBoxes[Constant.EWeekDay.eTue.ordinal()] = this.findViewById(R.id.alarm_setting_weekdays_2);
        this.checkBoxes[Constant.EWeekDay.eWed.ordinal()] = this.findViewById(R.id.alarm_setting_weekdays_3);
        this.checkBoxes[Constant.EWeekDay.eThu.ordinal()] = this.findViewById(R.id.alarm_setting_weekdays_4);
        this.checkBoxes[Constant.EWeekDay.eFri.ordinal()] = this.findViewById(R.id.alarm_setting_weekdays_5);
        this.checkBoxes[Constant.EWeekDay.eSat.ordinal()] = this.findViewById(R.id.alarm_setting_weekdays_6);

        this.initButtons();
    }
    private void initButtons() {
        int index=0;
        for (VIndexToggleButton checkBox: this.checkBoxes) { // init buttons
            checkBox.setChecked(this.mAlarm.getTime().isDayOfWeekChecked(index));
            if(checkBox.isChecked()){this.numberOfDaysChecked++;}
            checkBox.setOnCheckedChangeListener(this);
            checkBox.setIndex(index++);
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
        VIndexToggleButton vIndexToggleButton = (VIndexToggleButton) compoundButton;
        if (this.numberOfDaysChecked==1 && !isChecked) { // if no checkbox is checked
            vIndexToggleButton.setChecked(true); // recheck current checkbox
        } else { // at least one be checked
            if (this.mAlarm.getTime().isDayOfWeekChecked(vIndexToggleButton.getIndex()) != isChecked) {
                this.numberOfDaysChecked = (isChecked) ? this.numberOfDaysChecked + 1 : this.numberOfDaysChecked - 1;
                this.mAlarm.getTime().setDayOfWeekChecked(vIndexToggleButton.getIndex(), isChecked);
                this.interfaceSetAlarmDay.setAlarmDay();  // recompute alarm schedule
            }
        }
    }
}
