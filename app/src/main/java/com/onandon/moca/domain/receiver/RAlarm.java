package com.onandon.moca.domain.receiver;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import com.onandon.moca.R;
import com.onandon.moca.domain.activity.AAlarmCallback;
import com.onandon.moca.domain.model.AlarmModelTool;
import com.onandon.moca.domain.model.AlarmRepository;
import com.onandon.moca.domain.model.database.entity.Alarm;
import com.onandon.moca.domain.model.database.entity.MAlarmData;

import java.util.List;

public class RAlarm extends BroadcastReceiver {

    // Associate
    private AlarmRepository repository;
    private LiveData<List<Alarm>> alarms;

    // Component
    private AlarmModelTool tool;
    private Observer observer;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.repository = new AlarmRepository((Application) context.getApplicationContext());
        this.alarms = this.repository.getAlarms();
        this.tool = new AlarmModelTool(this.alarms, context);
        this.observer = o->update(context, intent);
        this.alarms.observeForever(this.observer);
    }
    private void update(Context context, Intent intent) {
        this.alarms.removeObserver(this.observer);
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            this.tool.scheduleNextAlarm();
        } else if (context.getResources().getString(R.string.alarm_action).equals(intent.getAction())){
            this.startAlarm(context, intent);
        }
    }
    private void startAlarm(Context context, Intent intent) {
        Bundle bundle = intent.getBundleExtra(context.getResources().getString(R.string.alarm_bundle));
        if (bundle != null) {
            int id = bundle.getInt(MAlarmData.class.getSimpleName());
            Alarm target = this.tool.findAlarmById(id);
            if (target != null && target.getMAlarm().isChecked()) { // if not removed && checked
                Bundle bundle2 = intent.getBundleExtra(context.getString(R.string.alarm_bundle));
                Intent newIntent = new Intent(context, AAlarmCallback.class);
                newIntent.putExtra(context.getString(R.string.alarm_bundle), bundle2);
                newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(newIntent);
            }
        }
    }
}