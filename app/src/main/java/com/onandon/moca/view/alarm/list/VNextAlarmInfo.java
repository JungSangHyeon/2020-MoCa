package com.onandon.moca.view.alarm.list;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.onandon.moca.R;
import com.onandon.moca.control.CAlarm;
import com.onandon.moca.model.MAlarm;
import com.onandon.moca.view.alarm.setting.VTime;

public class VNextAlarmInfo {

    // Associate
    private CAlarm cAlarm;
    private TextView time, date, dayOfWeek, name;

    // Constructor
    public VNextAlarmInfo(View view, CAlarm cAlarm) {
        this.cAlarm=cAlarm;
        this.time = view.findViewById(R.id.alarm_list_next_alarm_time);
        this.date = view.findViewById(R.id.alarm_list_next_alarm_date);
        this.dayOfWeek = view.findViewById(R.id.alarm_list_next_alarm_dayofweek);
        this.name = view.findViewById(R.id.alarm_list_next_alarm_name);
    }

    public void update() {
        MAlarm nextAlarm = this.cAlarm.getNextCloneAlarm();
        if(nextAlarm!=null){
            this.time.setText(nextAlarm.getTime().format(VTime.TIME_PATTERN));
            this.date.setText(nextAlarm.getTime().format(VTime.DAY_PATTERN));
            this.dayOfWeek.setText(nextAlarm.getTime().format(VTime.DAYOFWEEK_PATTERN));
            this.name.setText(nextAlarm.getName());
        }else{
            this.time.setText("알람이 없습니다");
            this.date.setText("");
            this.dayOfWeek.setText("");
            this.name.setText("");
        }
    }
}
