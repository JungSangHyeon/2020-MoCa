package com.onandon.moca.onAndOn.compoundView;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

import com.onandon.moca.R;
import com.onandon.moca.model.roomDatabase.entity.MAlarm;
import com.onandon.moca.onAndOn.oButton.oToggleButton.OIndexToggleButton;

import java.util.Calendar;

public class OWeekSelectView extends LinearLayout implements CompoundButton.OnCheckedChangeListener {

    public interface InterfaceSetAlarmDay { void setAlarmDay();}

    // Working Variable
    private int numberOfDaysChecked;

    // Associate
    private MAlarm mAlarm;
    private InterfaceSetAlarmDay interfaceSetAlarmDay;

    // Component
    private OIndexToggleButton[] checkBoxes;

    // Constructor
    public OWeekSelectView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.checkBoxes = new OIndexToggleButton[Calendar.DAY_OF_WEEK];
    }

    public void onViewCreated(InterfaceSetAlarmDay interfaceSetAlarmDay){
        this.interfaceSetAlarmDay=interfaceSetAlarmDay;

        // Associate View
        this.checkBoxes[Calendar.SUNDAY-1] = this.findViewById(R.id.alarm_setting_weekdays_0);
        this.checkBoxes[Calendar.MONDAY-1] = this.findViewById(R.id.alarm_setting_weekdays_1);
        this.checkBoxes[Calendar.TUESDAY-1] = this.findViewById(R.id.alarm_setting_weekdays_2);
        this.checkBoxes[Calendar.WEDNESDAY-1] = this.findViewById(R.id.alarm_setting_weekdays_3);
        this.checkBoxes[Calendar.THURSDAY-1] = this.findViewById(R.id.alarm_setting_weekdays_4);
        this.checkBoxes[Calendar.FRIDAY-1] = this.findViewById(R.id.alarm_setting_weekdays_5);
        this.checkBoxes[Calendar.SATURDAY-1] = this.findViewById(R.id.alarm_setting_weekdays_6);

        // Set View Callback
        int index=0;
        for (OIndexToggleButton checkBox: this.checkBoxes) { // init buttons
            checkBox.setOnCheckedChangeListener(this);
            checkBox.setIndex(index++);
        }
    }
    public void update(MAlarm mAlarm){
        this.mAlarm=mAlarm;
        for (OIndexToggleButton checkBox: this.checkBoxes) { // init buttons
            checkBox.setCheckedWithoutAnimation(this.mAlarm.getTime().isDayOfWeekChecked(checkBox.getIndex()));
            if(checkBox.isChecked()){this.numberOfDaysChecked++;}
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
        OIndexToggleButton vIndexToggleButton = (OIndexToggleButton) compoundButton;
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
