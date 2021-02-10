package com.onandon.moca.view.alarm.list;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.SharedPreferences;
import android.opengl.Visibility;
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
import com.onandon.moca.view.alarm.setting.VTime;

import java.util.Calendar;

public class VNextAlarmInfo implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    public interface VAlarmListUpdateCallback {
        void update();
    }

    // Working Variable
    private enum EMode{eAlarmTime, eLeftTime}
    private EMode mode;
    private String nowAlarmKey = "";

    // Associate
    private CAlarm cAlarm;
    private View view;
    private ConstraintLayout container;
    private TextView time, date, dayOfWeek, name;
    private OVectorAnimationToggleButton onOffButton;
    private View.OnClickListener editListener;
    private VAlarmListUpdateCallback updateCallback;
    View.OnLongClickListener removeListener;

    // Constructor
    public VNextAlarmInfo(View view, CAlarm cAlarm, View.OnClickListener editListener, VAlarmListUpdateCallback updateCallback,
                          View.OnLongClickListener removeListener) {
        this.mode = EMode.eAlarmTime;

        this.view=view;
        this.updateCallback=updateCallback;
        this.editListener=editListener;
        this.removeListener=removeListener;
        this.cAlarm=cAlarm;
        this.container = view.findViewById(R.id.alarm_list_next_alarm);
        this.time = view.findViewById(R.id.alarm_list_next_alarm_time);
        this.date = view.findViewById(R.id.alarm_list_next_alarm_date);
        this.dayOfWeek = view.findViewById(R.id.alarm_list_next_alarm_dayofweek);
        this.name = view.findViewById(R.id.alarm_list_next_alarm_name);
        this.onOffButton = view.findViewById(R.id.alarm_list_item_on);

        this.update();

        this.container.setOnClickListener(editListener);
        this.container.setOnLongClickListener(removeListener);
        this.time.setOnClickListener(this);
        this.onOffButton.setOnCheckedChangeListener(this);

        this.loadMode();
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

    static int i=0;
    public void updateWithAnimation() {
        MAlarm nextAlarm = this.cAlarm.getNextCloneAlarm();
        if((nextAlarm!=null && !nextAlarm.getKey().equals(this.nowAlarmKey)) || nextAlarm==null){
            OAnimator.animateVisibleToGone(this.time);
            OAnimator.animateVisibleToGone(this.date);
            OAnimator.animateVisibleToGone(this.dayOfWeek);
            OAnimator.animateVisibleToGone(this.name);
            ObjectAnimator animation = ObjectAnimator.ofFloat(this.onOffButton, "alpha", 0f);
            animation.setDuration(300);
            animation.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    update();
                    OAnimator.animateGoneToVisible(time);
                    OAnimator.animateGoneToVisible(date);
                    OAnimator.animateGoneToVisible(dayOfWeek);
                    OAnimator.animateGoneToVisible(name);
                    OAnimator.animateGoneToVisible(onOffButton);
                }
                @Override public void onAnimationStart(Animator animation) { }
                @Override public void onAnimationCancel(Animator animation) { }
                @Override public void onAnimationRepeat(Animator animation) { }
            });
            animation.start();
        }
    }

    public void update() {
        MAlarm nextAlarm = this.cAlarm.getNextCloneAlarm();
        if(nextAlarm!=null){
            this.nowAlarmKey = nextAlarm.getKey();
            this.container.setOnClickListener(this.editListener);
            this.container.setOnLongClickListener(this.removeListener);
            this.onOffButton.setVisibility(View.VISIBLE);
            this.onOffButton.setEnabled(true);
            this.onOffButton.setOnCheckedChangeListener(null);
            this.onOffButton.setCheckedWithoutAnimation(nextAlarm.isChecked());
            this.onOffButton.setOnCheckedChangeListener(this);
            if(this.mode == EMode.eAlarmTime){
                this.time.setText(nextAlarm.getTime().format(VTime.TIME_PATTERN));
            }else if(this.mode == EMode.eLeftTime){
                long nowTime = Calendar.getInstance().getTimeInMillis();
                long alarmTime = nextAlarm.getTime().getTimeInMillis();
                long leftTime = alarmTime - nowTime + 60*1000;
                long hour = leftTime / (60*60*1000);
                leftTime = leftTime%(60*60*1000);
                long minute = leftTime / (60*1000);
                String result = "";
                if(hour!=0){result+=hour+"시간 ";}
                result+=minute+"분 후";
                this.time.setText(result);
            }
            this.date.setText(nextAlarm.getTime().format(VTime.DAY_PATTERN));
            this.dayOfWeek.setText(nextAlarm.getTime().format(VTime.DAYOFWEEK_PATTERN));
            this.name.setText(nextAlarm.getName());
        }else{
            this.nowAlarmKey = "";
            this.time.setText("알람이 없습니다");
            this.date.setText("");
            this.dayOfWeek.setText("");
            this.name.setText("");
            this.container.setOnClickListener(null);
            this.container.setOnLongClickListener(null);
            this.onOffButton.setVisibility(View.GONE);
            this.onOffButton.setEnabled(false);
        }
    }

    public void saveMode() {
        SharedPreferences prefs = this.view.getContext().getSharedPreferences("VNextAlarmInfoMode", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("modeEnumIndex", this.mode.ordinal());
        editor.commit();
    }
    public void loadMode() {
        SharedPreferences prefs = this.view.getContext().getSharedPreferences("VNextAlarmInfoMode", Context.MODE_PRIVATE);
        int modeEnumIndex = prefs.getInt("modeEnumIndex", -1);
        if(modeEnumIndex!=-1){
            this.mode = EMode.values()[modeEnumIndex];
        }
    }
}
