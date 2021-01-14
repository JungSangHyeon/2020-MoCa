package com.onandon.moca.view.alarm.callback;

import android.os.Build;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.onandon.moca.Constant;
import com.onandon.moca.R;
import com.onandon.moca.activity.AAlarmCallback;
import com.onandon.moca.control.CAlarm;
import com.onandon.moca.model.MAlarm;
import com.onandon.moca.model.MReAlarm;
import com.onandon.moca.model.MSnooze;
import com.onandon.moca.technical.TAlarm;

import java.util.Locale;

@RequiresApi(api = Build.VERSION_CODES.N)
public class VAlarmCallBack implements View.OnClickListener{

    // Associate
    private TextView textviewName;
    private Button alarmOffBtn, snoozeBtn;
    private AAlarmCallback activity;
    private MAlarm mAlarm;

    // Component
    private TAlarm tAlarm;
    private CAlarm cAlarm;
    private Thread alarmDismissThread;

    // Constructor
    public VAlarmCallBack(AAlarmCallback activity) {
        this.activity = activity;

        this.cAlarm = new CAlarm(activity);

        this.textviewName = this.activity.findViewById(R.id.alarmcallback_name);

        this.alarmOffBtn = this.activity.findViewById(R.id.alarmcallback_off);
        this.alarmOffBtn.setOnClickListener(this);

        this.snoozeBtn = this.activity.findViewById(R.id.alarmcallback_snooze);
        this.snoozeBtn.setOnClickListener(this);

        this.tAlarm = new TAlarm(this.activity);
    }

    public void onCreate(MAlarm scheduledAlarm) {
        this.cAlarm.onCreate(Locale.KOREA);
        this.mAlarm = this.cAlarm.findByKey(scheduledAlarm.getKey());
        if (this.mAlarm != null) { // if not removed
            if (mAlarm.isChecked()) { // if enabled
                this.tAlarm.onCreate(mAlarm);
                this.tAlarm.onStartCommand();
                this.textviewName.setText(mAlarm.getName());
                MSnooze mSnooze = this.mAlarm.getmAlarmSnooze();
                if(mSnooze.isSnoozing()){ mSnooze.resetSnooze(); }
                this.alarmDismissThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try { Thread.sleep(Constant.AlarmRingMinute *1000*60); alarmMissed(); } catch (InterruptedException e) {}
                    }
                });
                this.alarmDismissThread.start();
            } else { // if disabled
                this.activity.finish();
            }
        } else { // if removed
            this.activity.finish();
        }
    }

    public void onDestroy() { this.cAlarm.onDestroy(); }

    @Override
    public void onClick(View view) {
        this.alarmDismissThread.interrupt();
        if (view == this.alarmOffBtn) { // alarm checked
            MReAlarm mAlarmReAlarm = this.mAlarm.getReAlarm();
            if(mAlarmReAlarm.isChecked()){ mAlarmReAlarm.resetReAlarm(); }
        }else if(view == this.snoozeBtn){ // snooze checked
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
        this.activity.finish();
    }
}
