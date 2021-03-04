package com.onandon.moca.domain.model;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.onandon.moca.R;
import com.onandon.moca.domain.model.database.entity.Alarm;
import com.onandon.moca.domain.model.database.entity.MAlarmData;
import com.onandon.moca.domain.receiver.RAlarm;

import java.util.List;
import java.util.Vector;

public class AlarmModelTool {

    // Associate
    private LiveData<List<Alarm>> alarms;
    private Context context;

    // Constructor
    public AlarmModelTool(LiveData<List<Alarm>> alarms, Context context) { this.alarms=alarms; this.context=context; }

    public Alarm getAlarm(int index){ return this.alarms.getValue().get(index); }
    public Alarm getNextAlarm(){
        int nextAlarmIndex = this.getNextAlarmIndex();
        if (nextAlarmIndex != -1) { return this.alarms.getValue().get(nextAlarmIndex); }
        else{ return null; }
    }
    public Alarm findAlarmById(int id) {
        for(Alarm alarm : this.alarms.getValue()){
            if(alarm.getId()==id) return alarm;
        }
        return null;
    }

    public void scheduleNextAlarm(){
        Alarm nextAlarm = this.getNextAlarm();
        if(nextAlarm!=null){ this.scheduleAlarm(this.context, nextAlarm); }
    }
    public void scheduleNextAlarm(Vector<Alarm> modifiedAlarms){
        if(this.getNextAlarmIndex(modifiedAlarms)!=-1){
            this.scheduleAlarm(this.context, modifiedAlarms.get(this.getNextAlarmIndex(modifiedAlarms)));
        }
    }

    public int getNextAlarmIndex() {
        Vector<Alarm> alarms = new Vector<>();
        alarms.addAll(this.alarms.getValue());
        return this.getNextAlarmIndex(alarms);
    }
    public int getNextAlarmIndex(Vector<Alarm> modifiedAlarm) {
        int index = -1;
        long minTime = Long.MAX_VALUE;
        for (int i = 0; i < modifiedAlarm.size(); i++) {
            MAlarmData cloneAlarm = modifiedAlarm.get(i).getMAlarm().schedulerNextAlarm();
            if (cloneAlarm.getAlarmTime() < minTime && cloneAlarm.isChecked()) {
                index = i;
                minTime = cloneAlarm.getAlarmTime();
            }
        }
        return index;
    }

    public void scheduleAlarm(Context context, Alarm target) {
        Log.d("TEST1131", "AlarmModelTool schedule "+target.getId());
        MAlarmData nextCloneAlarm = target.getMAlarm().schedulerNextAlarm();
        if (nextCloneAlarm != null){
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            Bundle bundle = new Bundle();
            bundle.putInt(MAlarmData.class.getSimpleName(), target.getId());
            Intent intentStartAlarm = new Intent(context, RAlarm.class);
            intentStartAlarm.setAction(context.getResources().getString(R.string.alarm_action));
            intentStartAlarm.putExtra(context.getResources().getString(R.string.alarm_bundle), bundle);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intentStartAlarm, PendingIntent.FLAG_UPDATE_CURRENT);
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, nextCloneAlarm.getAlarmTime(), pendingIntent);
        }
    }
}
