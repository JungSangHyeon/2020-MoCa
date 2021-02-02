package com.onandon.moca.view.alarm.list;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.appbar.AppBarLayout;
import com.onandon.moca.R;
import com.onandon.moca.control.CAlarm;
import com.onandon.moca.model.MAlarm;
import com.onandon.moca.view.alarm.setting.VTime;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class VNextAlarmInfo implements View.OnClickListener {

    // Working Variable
    private enum EMode{eAlarmTime, eLeftTime}
    private EMode mode;

    // Associate
    private CAlarm cAlarm;
    private AppBarLayout appBarLayout;
    private TextView time, date, dayOfWeek, name;

    // Constructor
    public VNextAlarmInfo(View view, CAlarm cAlarm) {
        this.mode = EMode.eAlarmTime;

        this.cAlarm=cAlarm;
        this.appBarLayout = view.findViewById(R.id.next_alarm_info);
        this.time = view.findViewById(R.id.alarm_list_next_alarm_time);
        this.date = view.findViewById(R.id.alarm_list_next_alarm_date);
        this.dayOfWeek = view.findViewById(R.id.alarm_list_next_alarm_dayofweek);
        this.name = view.findViewById(R.id.alarm_list_next_alarm_name);

        this.time.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Log.d("TEST", "onClick");
        this.mode = EMode.values()[(this.mode.ordinal()+1 == EMode.values().length)? 0:this.mode.ordinal()+1];
        this.update();
    }

    public void update() {
        MAlarm nextAlarm = this.cAlarm.getNextCloneAlarm();
        if(nextAlarm!=null){
            if(this.mode == EMode.eAlarmTime){
                this.time.setText(nextAlarm.getTime().format(VTime.TIME_PATTERN));
                this.date.setText(nextAlarm.getTime().format(VTime.DAY_PATTERN));
                this.dayOfWeek.setText(nextAlarm.getTime().format(VTime.DAYOFWEEK_PATTERN));
            }else if(this.mode == EMode.eLeftTime){
                long nowTime = Calendar.getInstance().getTimeInMillis();
                long alarmTime = nextAlarm.getTime().getTimeInMillis();
                long leftTime = alarmTime - nowTime + 60*1000;
                long day = leftTime / (24*60*60*1000);
                leftTime = leftTime%(24*60*60*1000);
                long hour = leftTime / (60*60*1000);
                leftTime = leftTime%(60*60*1000);
                long minute = leftTime / (60*1000);
                String result = "";
                if(day!=0){result+=day+"일 ";}
                if(hour!=0){result+=hour+"시간 ";}
                result+=minute+"분 후";
                this.time.setText(result);
                this.date.setText("");
                this.dayOfWeek.setText("");
            }
            this.name.setText(nextAlarm.getName());
        }else{
            this.time.setText("알람이 없습니다");
            this.date.setText("");
            this.dayOfWeek.setText("");
            this.name.setText("");
        }
    }


}
