package com.onandon.moca.domain.model;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.onandon.moca.domain.model.database.entity.Alarm;
import com.onandon.moca.onAndOnAsset.model.OViewModel;

import java.util.List;
import java.util.Vector;

public class AlarmViewModel extends AndroidViewModel implements OViewModel {

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
    }

    public LiveData<List<Alarm>> getAlarms() { return this.alarms; }
    public void insert(Alarm alarm) {   this.preInsert(alarm); this.mRepository.insert(alarm); }
    public void update(Alarm...alarms) { this.preUpdate(alarms); this.mRepository.update(alarms);}
    public void delete(Alarm alarm) {  this.preDelete(alarm); this.mRepository.delete(alarm); }

    @Override public LiveData getLiveData() { return this.getAlarms(); }

    public Alarm getAlarm(int index){ return this.tool.getAlarm(index); }
    public Alarm getNextAlarm(){ return this.tool.getNextAlarm();}
    public int getNextAlarmIndex() { return this.tool.getNextAlarmIndex(); }
    public Alarm findAlarmById(int id) { return this.tool.findAlarmById(id);}
    public void scheduleNextAlarm(){ this.tool.scheduleNextAlarm();}
    public void scheduleNextAlarm(Vector<Alarm> modifiedAlarms){ this.tool.scheduleNextAlarm(modifiedAlarms); }

    private void preInsert(Alarm deleteAlarm){
        Vector<Alarm> modifiedAlarms = new Vector<>();
        modifiedAlarms.addAll(this.getAlarms().getValue());
        modifiedAlarms.add(deleteAlarm);
        this.updateFold(modifiedAlarms);
        this.scheduleNextAlarm(modifiedAlarms);
    }
    private void preUpdate(Alarm...alarms){
        Vector<Alarm> modifiedAlarms = new Vector<>();
        for(Alarm alarm : this.getAlarms().getValue()){
            boolean saveModified = false;
            for(Alarm modifiedAlarm : alarms){
                if(alarm.getId()==modifiedAlarm.getId()){
                    modifiedAlarms.add(modifiedAlarm);
                    saveModified = true;
                }
            }
            if(!saveModified){
                modifiedAlarms.add(alarm);
            }
        }
        this.updateFold(modifiedAlarms);
        this.scheduleNextAlarm(modifiedAlarms);
    }
    private void preDelete(Alarm deleteAlarm){
        Vector<Alarm> modifiedAlarms = new Vector<>();
        for(Alarm alarm : this.getAlarms().getValue()){
            if(alarm.getId()!=deleteAlarm.getId()){
                modifiedAlarms.add(alarm);
            }
        }
        this.updateFold(modifiedAlarms);
        this.scheduleNextAlarm(modifiedAlarms);
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
}
