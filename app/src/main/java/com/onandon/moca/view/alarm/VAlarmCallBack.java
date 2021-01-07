package com.onandon.moca.view.alarm;

import android.os.Build;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.onandon.moca.R;
import com.onandon.moca.activity.AAlarmCallback;
import com.onandon.moca.control.CAlarm;
import com.onandon.moca.model.MAlarm;
import com.onandon.moca.technical.TAlarm;

import java.util.Locale;

public class VAlarmCallBack implements View.OnClickListener{

    private final TextView textviewName;
    private final Button alarmOffBtn;
    private final Button snoozeBtn;

    private final AAlarmCallback activity;
    private MAlarm mAlarm;

    private final CAlarm cAlarm;
    private final TAlarm tAlarm;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public VAlarmCallBack(AAlarmCallback activity) {
        this.activity = activity;

        this.cAlarm = new CAlarm(activity);

        this.textviewName = this.activity.findViewById(R.id.alarmcallback_name);

        this.alarmOffBtn = this.activity.findViewById(R.id.alarmcallback_off);
        this.alarmOffBtn.setOnClickListener(this);

        this.snoozeBtn = this.activity.findViewById(R.id.alarmcallback_off);
        this.snoozeBtn.setOnClickListener(this);

        this.tAlarm = new TAlarm(this.activity);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void onCreate(MAlarm scheduledAlarm) {
        this.cAlarm.onCreate(Locale.KOREA);
        this.mAlarm = this.cAlarm.findByKey(scheduledAlarm.getKey());
        // if not removed
        if (this.mAlarm != null) {
            // if enabled
            if (mAlarm.isChecked()) {
                this.tAlarm.onCreate(mAlarm);
                this.tAlarm.onStartCommand();
                this.textviewName.setText(mAlarm.getName());
            } // if disabled
            else {
                this.activity.finish();
            }
        } // if removed
        else {
            this.activity.finish();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void onDestroy() {
        Toast.makeText(this.activity, "VAlarmCallBack::onDestroy", Toast.LENGTH_LONG).show();
        this.cAlarm.onDestroy();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onClick(View view) {
        if (view == this.alarmOffBtn) {
            if (mAlarm.getTime().isRecurringChecked()) {
                cAlarm.schedulerAnAlarm(this.mAlarm);
            }
            this.tAlarm.onStopCommand();
            this.activity.finish();
        }else if(view == this.snoozeBtn){
            // TODO Snooze
        }
    }
}
