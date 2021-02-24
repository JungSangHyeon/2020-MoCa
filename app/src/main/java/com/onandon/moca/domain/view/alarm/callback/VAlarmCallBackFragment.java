package com.onandon.moca.domain.view.alarm.callback;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelStoreOwner;

import com.onandon.moca.domain.Constant;
import com.onandon.moca.R;
import com.onandon.moca.domain.model.AlarmViewModel;
import com.onandon.moca.domain.model.database.entity.Alarm;
import com.onandon.moca.domain.model.database.entity.MAlarmData;
import com.onandon.moca.domain.model.database.entity.MReAlarm;
import com.onandon.moca.domain.model.database.entity.MSnooze;
import com.onandon.moca.onAndOnAsset.view.comp.fragment.OFragment;
import com.onandon.moca.domain.technical.TAlarm;

public class VAlarmCallBackFragment extends OFragment<AlarmViewModel> implements View.OnClickListener{

    // Associate
        // Model
        private Alarm target;
        private MAlarmData mAlarmData;
        // View
        private TextView name;
        private Button alarmOffButton, snoozeButton;

    // Component
    private TAlarm tAlarm;
    private Thread alarmDismissThread;

    /**
     * System Callback
     */
    @Override protected void createComponent() { this.tAlarm = new TAlarm(); }
    @Override public void onCreate(Activity activity) { this.tAlarm.onCreate(activity); }
    @Override public ViewModelStoreOwner getViewModelStoreOwner() { return this.requireActivity(); }
    @Override public Class<? extends ViewModel> getModel() { return AlarmViewModel.class; }
    @Override public int getLayoutId() { return R.layout.alarmcallback_main; }
    @Override
    public void associateAndInitView(View view) {
        this.name = view.findViewById(R.id.alarmcallback_name);
        this.alarmOffButton = view.findViewById(R.id.alarmcallback_off);
        this.snoozeButton = view.findViewById(R.id.alarmcallback_snooze);

        this.alarmOffButton.setOnClickListener(this);
        this.snoozeButton.setOnClickListener(this);
    }

    /**
     * Update
     */
    @Override
    protected void update() {
        this.removeModelObserver();
        Bundle bundle = this.getActivity().getIntent().getBundleExtra(this.getResources().getString(R.string.alarm_bundle));
        if (bundle != null) {
            int id = bundle.getInt(MAlarmData.class.getSimpleName());
            this.target = this.model.findAlarmById(id);
//            if (this.target != null && this.target.getMAlarm().isChecked()) { // if not removed && checked
                this.mAlarmData = this.target.getMAlarm();
                this.tAlarm.setTargetMAlarm(this.mAlarmData);
                this.tAlarm.onStartCommand();
                this.name.setText(this.mAlarmData.getName());
                MSnooze mSnooze = this.mAlarmData.getmAlarmSnooze();
                if(mSnooze.isSnoozing()){ mSnooze.resetSnooze(); this.model.update(target);}
                this.alarmDismissThread = new Thread(() -> {
                    try { Thread.sleep(Constant.AlarmRingMinute *1000*60); this.alarmMissed(); }
                    catch (InterruptedException e) {}
                });
                this.alarmDismissThread.start();
//            } else { // if removed || not checked
//                this.finish();
//            }
        }
    }

    /**
     * Callback
     */
    @Override
    public void onClick(View view) { // for alarm off button & snooze button
        this.alarmDismissThread.interrupt();
        if (view == this.alarmOffButton) { // alarm checked
            MReAlarm mAlarmReAlarm = this.mAlarmData.getReAlarm();
            if(mAlarmReAlarm.isChecked()){ mAlarmReAlarm.resetReAlarm();}
        }else if(view == this.snoozeButton){ // snooze checked
            this.mAlarmData.getmAlarmSnooze().startSnooze();
        }
        this.alarmOff();
    }
    private void alarmMissed() {
        MReAlarm mAlarmReAlarm = this.mAlarmData.getReAlarm();
        if(mAlarmReAlarm.isReAlarmOn()) {
            if(!mAlarmReAlarm.isReAlarming()){ mAlarmReAlarm.startReAlarm(); }
            mAlarmReAlarm.updateReAlarm();
        }
        this.alarmOff();
    }
    private void alarmOff() {
        this.tAlarm.onStopCommand();
        this.model.update(this.target);
        this.finish();
    }
    private void finish() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) { this.getActivity().finishAndRemoveTask(); }
        else { this.getActivity().finish(); System.exit(0); }
    }
}
