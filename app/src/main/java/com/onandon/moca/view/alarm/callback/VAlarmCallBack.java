package com.onandon.moca.view.alarm.callback;

import android.os.Build;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.onandon.moca.Constant;
import com.onandon.moca.R;
import com.onandon.moca.activity.AAlarmCallback;
import com.onandon.moca.control.CAlarm;
import com.onandon.moca.model.MAlarm;
import com.onandon.moca.model.MReAlarm;
import com.onandon.moca.model.MSnooze;
import com.onandon.moca.technical.TAlarm;

import java.util.Locale;

public class VAlarmCallBack implements View.OnClickListener{

    // Associate
    private AAlarmCallback activity;
    // Associate View
    private TextView name;
    private Button alarmOffButton, snoozeButton;
    private MAlarm mAlarm;

    // Component
    private TAlarm tAlarm;
    private CAlarm cAlarm;
    private Thread alarmDismissThread;

    // Constructor
    public VAlarmCallBack(AAlarmCallback activity) {
        // Associate
        this.activity = activity;
        this.name = activity.findViewById(R.id.alarmcallback_name);
        this.alarmOffButton = activity.findViewById(R.id.alarmcallback_off);
        this.snoozeButton = activity.findViewById(R.id.alarmcallback_snooze);

        // Create Component
        this.cAlarm = new CAlarm(activity);
        this.tAlarm = new TAlarm(activity);

        // Init Associate
        this.alarmOffButton.setOnClickListener(this::onClick);
        this.snoozeButton.setOnClickListener(this::onClick);
    }

    public void onCreate(MAlarm scheduledAlarm) {
        this.cAlarm.onCreate(Locale.KOREA);
        this.mAlarm = this.cAlarm.findByKey(scheduledAlarm.schedulerNextAlarm().getKey());
        if (this.mAlarm != null && this.mAlarm.isChecked()) { // if not removed && checked
            this.tAlarm.onCreate(this.mAlarm);
            this.tAlarm.onStartCommand();
            this.name.setText(this.mAlarm.getName());
            MSnooze mSnooze = this.mAlarm.getmAlarmSnooze();
            if(mSnooze.isSnoozing()){ mSnooze.resetSnooze(); }
            this.alarmDismissThread = new Thread(() -> {
                try { Thread.sleep(Constant.AlarmRingMinute *1000*60); this.alarmMissed(); }
                catch (InterruptedException e) {}
            });
            this.alarmDismissThread.start();
        } else { // if removed || not checked
            this.activity.finish();
        }
    }
    public void onDestroy() { this.cAlarm.onDestroy(); }

    @Override
    public void onClick(View view) { // for alarm off button & snooze button
        this.alarmDismissThread.interrupt();
        if (view == this.alarmOffButton) { // alarm checked
            MReAlarm mAlarmReAlarm = this.mAlarm.getReAlarm();
            if(mAlarmReAlarm.isChecked()){ mAlarmReAlarm.resetReAlarm(); }
        }else if(view == this.snoozeButton){ // snooze checked
            this.mAlarm.getmAlarmSnooze().startSnooze();
        }
        this.alarmOff();
    }
    private void alarmMissed() {
        MReAlarm mAlarmReAlarm = this.mAlarm.getReAlarm();
        if(mAlarmReAlarm.isReAlarmOn()){
            if(!mAlarmReAlarm.isReAlarming()){ mAlarmReAlarm.startReAlarm(); }
            mAlarmReAlarm.updateReAlarm();
        }
        this.alarmOff();
    }

    private void alarmOff() {
        this.tAlarm.onStopCommand();
        this.cAlarm.scheduleAlarm();
        this.cAlarm.store();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            this.activity.finishAndRemoveTask();
        } else {
            this.activity.finish();
            System.exit(0);
        }
    }
}
