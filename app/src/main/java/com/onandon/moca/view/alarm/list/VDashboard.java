package com.onandon.moca.view.alarm.list;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.onandon.moca.R;
import com.onandon.moca.control.CAlarm;
import com.onandon.moca.model.MAlarm;
import com.onandon.moca.onAndOn.OAnimator;
import com.onandon.moca.onAndOn.oButton.oToggleButton.OVectorAnimationToggleButton;
import com.onandon.moca.technical.device.TEarphone;
import com.onandon.moca.view.alarm.setting.VTime;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class VDashboard implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    // Working Variable
    private enum EMode{eAlarmTime, eLeftTime}
    private EMode mode;
    private String nowAlarmKey = "";
    private long nowAlarmTime;

    // Associate
    private CAlarm cAlarm;
    private View view;
    private ConstraintLayout container;
    private TextView time, date, dayOfWeek, name, earphone;
    private OVectorAnimationToggleButton onOffButton;
    private View.OnClickListener editListener;
    private UpdateCallback updateCallback;
    View.OnLongClickListener removeListener;

    // Constructor
    public VDashboard(View view, CAlarm cAlarm,
                      View.OnClickListener editListener, UpdateCallback updateCallback, View.OnLongClickListener removeListener) {
        this.view=view;
        this.loadMode();

        this.cAlarm=cAlarm;
        this.editListener=editListener;
        this.updateCallback=updateCallback;
        this.removeListener=removeListener;
        this.container = view.findViewById(R.id.alarm_list_dashboard);
        this.time = view.findViewById(R.id.alarm_list_dashboard_time);
        this.date = view.findViewById(R.id.alarm_list_dashboard_date);
        this.dayOfWeek = view.findViewById(R.id.alarm_list_dashboard_dayofweek);
        this.name = view.findViewById(R.id.alarm_list_dashboard_name);
        this.onOffButton = view.findViewById(R.id.alarm_list_dashboard_switch);
        this.earphone = view.findViewById(R.id.alarm_list_earphone);
        this.earphone.setVisibility(TEarphone.isEarphoneConnected(view.getContext())? View.VISIBLE : View.INVISIBLE);

        this.container.setOnClickListener(editListener);
        this.container.setOnLongClickListener(removeListener);
        this.time.setOnClickListener(this);
        this.onOffButton.setOnCheckedChangeListener(this);

        this.update();
    }

    @Override
    public void onClick(View v) { // for time
        this.mode = EMode.values()[(this.mode.ordinal()+1 == EMode.values().length)? 0:this.mode.ordinal()+1];
        this.saveMode();
        this.update();
    }
    @Override // for on off button
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(this.cAlarm.getNextAlarmIndex()!=-1){
            this.cAlarm.getAlarm(this.cAlarm.getNextAlarmIndex()).setChecked(isChecked);
            this.cAlarm.store();
            this.cAlarm.scheduleAlarm();
            this.updateCallback.update();
        }
    }

    public void updateWithAnimation() {
        MAlarm nextAlarm = this.cAlarm.getNextCloneAlarm();
//        if((nextAlarm!=null && !nextAlarm.getKey().equals(this.nowAlarmKey)) || nextAlarm==null){
        if((nextAlarm!=null && nextAlarm.getAlarmTime()!=this.nowAlarmTime) || nextAlarm==null){
            Animator.AnimatorListener animatorListener = new Animator.AnimatorListener() {
                @Override public void onAnimationEnd(Animator animation) {
                    update();
                    OAnimator.animateAlphaChange(300, 1, null, time, date, dayOfWeek, name, onOffButton);
                }
                @Override public void onAnimationStart(Animator animation) { }
                @Override public void onAnimationCancel(Animator animation) { }
                @Override public void onAnimationRepeat(Animator animation) { }
            };
            OAnimator.animateAlphaChange(300, 0, animatorListener, this.time, this.date, this.dayOfWeek, this.name, this.onOffButton);
        }
    }

    public void update() {
        MAlarm nextAlarm = this.cAlarm.getNextCloneAlarm();
        if(nextAlarm!=null){
//            this.nowAlarmKey = nextAlarm.getKey();
            nowAlarmTime = nextAlarm.getAlarmTime();
            this.container.setOnClickListener(this.editListener);
            this.container.setOnLongClickListener(this.removeListener);
            this.onOffButton.setVisibility(View.VISIBLE);
            this.onOffButton.setEnabled(true);
            this.onOffButton.setOnCheckedChangeListener(null);
            this.onOffButton.setCheckedWithoutAnimation(nextAlarm.isChecked());
            this.onOffButton.setOnCheckedChangeListener(this);

            long alarmTime = nextAlarm.getAlarmTime();

            if(this.mode == EMode.eAlarmTime){
                SimpleDateFormat simpleDateFormat = (SimpleDateFormat) SimpleDateFormat.getInstance();
                simpleDateFormat.applyPattern(VTime.TIME_PATTERN);
                this.time.setText(simpleDateFormat.format(new Date(alarmTime)));
            }else if(this.mode == EMode.eLeftTime){
                long nowTime = Calendar.getInstance().getTimeInMillis();
                long leftTime = alarmTime - nowTime + 60*1000;
                long hour = leftTime / (60*60*1000);
                leftTime = leftTime%(60*60*1000);
                long minute = leftTime / (60*1000);
                String result = "";
                if(hour!=0){result+=hour+this.view.getContext().getResources().getString(R.string.hour)+" ";}
                result+=minute+this.view.getContext().getResources().getString(R.string.minute)+" "+this.view.getContext().getResources().getString(R.string.later);
                this.time.setText(result);
            }
            this.date.setText(nextAlarm.getTime().format(VTime.DAY_PATTERN));
            this.dayOfWeek.setText(nextAlarm.getTime().format(VTime.DAYOFWEEK_PATTERN));
            this.name.setText(nextAlarm.getName());
        }else{
            this.nowAlarmKey = "";
            nowAlarmTime = -1;
            this.time.setText(this.view.getContext().getResources().getString(R.string.no_alarm));
            this.date.setText("");
            this.dayOfWeek.setText("");
            this.name.setText("");
            this.container.setOnClickListener(null);
            this.container.setOnLongClickListener(null);
            this.onOffButton.setVisibility(View.GONE);
            this.onOffButton.setEnabled(false);
        }
    }

    /**
     * Save & Load Mode
     */
    public void saveMode() {
        SharedPreferences prefs = this.view.getContext().getSharedPreferences("VNextAlarmInfoMode", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("modeEnumIndex", this.mode.ordinal());
        editor.commit();
    }
    public void loadMode() {
        SharedPreferences prefs = this.view.getContext().getSharedPreferences("VNextAlarmInfoMode", Context.MODE_PRIVATE);
        int modeEnumIndex = prefs.getInt("modeEnumIndex", -1);
        this.mode = (modeEnumIndex!=-1)? EMode.values()[modeEnumIndex]:EMode.eAlarmTime;
    }
}
