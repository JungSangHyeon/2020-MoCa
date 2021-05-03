package com.onandon.moca.domain.model;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.onandon.moca.domain.model.database.entity.Alarm;
import com.onandon.moca.domain.service.SAlarmSetNotification;
import com.onandon.moca.onAndOnAsset.model.OViewModel;

import java.util.List;
import java.util.Vector;

public class AlarmViewModel extends AndroidViewModel implements OViewModel {

    // Working Variable
    private boolean updateTime = false;

    // Associate
    private final LiveData<List<Alarm>> alarms;

    // Component
    private AlarmRepository mRepository;
    private AlarmModelTool tool;

    // Constructor
    public AlarmViewModel(Application application) {
        super(application);
        this.mRepository = new AlarmRepository(application);
        this.alarms = this.mRepository.getAlarms();
        this.tool = new AlarmModelTool(this.alarms, this.getApplication().getApplicationContext());
        this.alarms.observeForever(o->this.afterModify());
    }

    public void insert(Alarm alarm) { this.mRepository.insert(alarm); this.updateTime = true; }
    public void update(Alarm... alarms) { this.mRepository.update(alarms); this.updateTime = true; }
    public void delete(Alarm alarm) { this.mRepository.delete(alarm); this.updateTime = true; }

    boolean first = true;
    private void afterModify() {
        if(first){
            first=false;
            this.updateService();
        }
        if(this.updateTime){
            Vector<Alarm> modifiedAlarms = new Vector<>();
            modifiedAlarms.addAll(this.getAlarms().getValue());
            this.updateFold(modifiedAlarms);
            this.scheduleNextAlarm(modifiedAlarms);
            this.updateService();
        }
        this.updateTime = false;
    }

    int showingAlarmId = -1;
    public void stopService(){
        Intent intent = new Intent(this.getApplication().getApplicationContext(), SAlarmSetNotification.class);
        this.getApplication().getApplicationContext().stopService(intent);
    }
    public void startService(){
        Intent intent = new Intent(this.getApplication().getApplicationContext(), SAlarmSetNotification.class);
        intent.putExtra("NextAlarmInfo", this.getNextAlarm().getAlarmData());
        if (Build.VERSION.SDK_INT >= 26) { this.getApplication().getApplicationContext().startForegroundService(intent); }
        else { this.getApplication().getApplicationContext().startService(intent); }
    }
    private void updateService() {
        if(this.isMyServiceRunning(SAlarmSetNotification.class) && this.getNextAlarm()==null){ // off
            this.stopService();
        } else if(this.isMyServiceRunning(SAlarmSetNotification.class) && this.getNextAlarm()!=null){
            if(showingAlarmId!=this.getNextAlarm().getId()){
                showingAlarmId = this.getNextAlarm().getId();
                this.stopService();
            }
           this.startService();
        } else if(!this.isMyServiceRunning(SAlarmSetNotification.class) && this.getNextAlarm()!=null) { // on
            this.startService();
        }
    }
    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) this.getApplication().getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) { return true; }
        }
        return false;
    }

    private void updateFold(Vector<Alarm> modifiedAlarms) {
        for(Alarm alarm : modifiedAlarms){
            if(alarm.isFold()){
                alarm.setFold(false);
                this.mRepository.update(alarm);
            }
        }
        if(this.tool.getNextAlarmIndex(modifiedAlarms)!=-1){
            modifiedAlarms.get(this.tool.getNextAlarmIndex(modifiedAlarms)).setFold(true);
            this.mRepository.update(modifiedAlarms.get(this.tool.getNextAlarmIndex(modifiedAlarms)));
        }
    }
    @Override public LiveData getLiveData() { return this.getAlarms(); }
    public LiveData<List<Alarm>> getAlarms() { return this.alarms; }
    public Alarm getAlarm(int index){ return this.tool.getAlarm(index); }
    public Alarm getNextAlarm(){ return this.tool.getNextAlarm();}
    public int getNextAlarmIndex() { return this.tool.getNextAlarmIndex(); }
    public Alarm findAlarmById(int id) { return this.tool.findAlarmById(id);}
    public void scheduleNextAlarm(){ this.tool.scheduleNextAlarm();}
    public void scheduleNextAlarm(Vector<Alarm> modifiedAlarms){ this.tool.scheduleNextAlarm(modifiedAlarms); }
}
