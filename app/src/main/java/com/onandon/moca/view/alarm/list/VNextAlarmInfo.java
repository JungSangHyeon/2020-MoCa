package com.onandon.moca.view.alarm.list;

import android.opengl.Visibility;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.onandon.moca.R;
import com.onandon.moca.control.CAlarm;
import com.onandon.moca.model.MAlarm;
import com.onandon.moca.onAndOn.oButton.oToggleButton.OVectorAnimationToggleButton;
import com.onandon.moca.view.alarm.setting.VTime;

import java.util.Calendar;

public class VNextAlarmInfo implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

        public interface VAlarmListUpdateCallback {
        void update();
    }

    // Working Variable
    private enum EMode{eAlarmTime, eLeftTime}
    private EMode mode;

    // Associate
    private CAlarm cAlarm;
    private ConstraintLayout container;
    private TextView time, date, dayOfWeek, name;
    private OVectorAnimationToggleButton onOffButton;
    private View.OnClickListener editListener;
    VAlarmListUpdateCallback updateCallback;

    // Constructor
    public VNextAlarmInfo(View view, CAlarm cAlarm, View.OnClickListener editListener, VAlarmListUpdateCallback updateCallback) {
        this.mode = EMode.eAlarmTime;

        this.updateCallback=updateCallback;
        this.editListener=editListener;
        this.cAlarm=cAlarm;
        this.container = view.findViewById(R.id.alarm_list_next_alarm);
        this.time = view.findViewById(R.id.alarm_list_next_alarm_time);
        this.date = view.findViewById(R.id.alarm_list_next_alarm_date);
        this.dayOfWeek = view.findViewById(R.id.alarm_list_next_alarm_dayofweek);
        this.name = view.findViewById(R.id.alarm_list_next_alarm_name);
        this.onOffButton = view.findViewById(R.id.alarm_list_item_on);

        this.container.setOnClickListener(editListener);
        this.time.setOnClickListener(this);
        this.onOffButton.setOnCheckedChangeListener(this);
    }

    @Override
    public void onClick(View v) { // for time
        this.mode = EMode.values()[(this.mode.ordinal()+1 == EMode.values().length)? 0:this.mode.ordinal()+1];
        this.update();
    }
    @Override // for on off button
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        this.cAlarm.getAlarm(this.cAlarm.getNextAlarmIndex()).setChecked(isChecked);
        this.cAlarm.store();
        this.cAlarm.scheduleAlarm();
        this.updateCallback.update();
    }

    public void update() {
        MAlarm nextAlarm = this.cAlarm.getNextCloneAlarm();
        if(nextAlarm!=null){
            this.container.setOnClickListener(this.editListener);
            this.onOffButton.setVisibility(View.VISIBLE);
            this.onOffButton.setEnabled(true);
            this.onOffButton.setChecked(nextAlarm.isChecked());
            if(this.mode == EMode.eAlarmTime){
                this.time.setText(nextAlarm.getTime().format(VTime.TIME_PATTERN));
            }else if(this.mode == EMode.eLeftTime){
                long nowTime = Calendar.getInstance().getTimeInMillis();
                long alarmTime = nextAlarm.getTime().getTimeInMillis();
                long leftTime = alarmTime - nowTime + 60*1000;
//                long day = leftTime / (24*60*60*1000);
//                leftTime = leftTime%(24*60*60*1000);
                long hour = leftTime / (60*60*1000);
                leftTime = leftTime%(60*60*1000);
                long minute = leftTime / (60*1000);
                String result = "";
//                if(day!=0){result+=day+"일 ";}
                if(hour!=0){result+=hour+"시간 ";}
                result+=minute+"분 후";
                this.time.setText(result);
            }
            this.date.setText(nextAlarm.getTime().format(VTime.DAY_PATTERN));
            this.dayOfWeek.setText(nextAlarm.getTime().format(VTime.DAYOFWEEK_PATTERN));
            this.name.setText(nextAlarm.getName());
        }else{
            this.time.setText("알람이 없습니다");
            this.date.setText("");
            this.dayOfWeek.setText("");
            this.name.setText("");
            this.container.setOnClickListener(null);
            this.onOffButton.setVisibility(View.GONE);
            this.onOffButton.setEnabled(false);
        }
    }


}
