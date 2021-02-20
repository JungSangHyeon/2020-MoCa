package com.onandon.moca.control;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.onandon.moca.Constant;
import com.onandon.moca.model.roomDatabase.entity.MAlarm;
import com.onandon.moca.technical.DataAccessObject;

import java.util.Vector;

public class MViewModel extends ViewModel {

    // Component
    private DataAccessObject dataAccessObject;
    private MutableLiveData<Vector<MAlarm>> mAlarmsMutableLiveData;

    public void onCreate(Context context) {
        if (this.mAlarmsMutableLiveData == null) {
            this.dataAccessObject = new DataAccessObject(context);
            this.mAlarmsMutableLiveData = new MutableLiveData<>();
            this.loadMAlarms();
        }
    }
    private void loadMAlarms() {
        this.mAlarmsMutableLiveData.setValue(this.dataAccessObject.read(Constant.DefaultFileName));
        if(this.mAlarmsMutableLiveData.getValue() == null){ this.mAlarmsMutableLiveData.setValue(new Vector<>()); }
    }
    private void saveMAlarms() {
        this.dataAccessObject.save(Constant.DefaultFileName, this.mAlarmsMutableLiveData.getValue());
        this.loadMAlarms();
    }
    @Override
    protected void onCleared() { // call when activity finished
        this.saveMAlarms();
    }

    public void savee(){
        this.onCleared();
    }
    ////////////////////////////////////////////////////////////

    public void notifyValueUpdated() { // call observe
        this.mAlarmsMutableLiveData.setValue(this.mAlarmsMutableLiveData.getValue());
    }

    ////////////////////////////////////////////////////////////

    public Vector<MAlarm> getMAlarms(){
        return this.mAlarmsMutableLiveData.getValue();
    }
    public LiveData<Vector<MAlarm>> getMAlarmsLiveData(){
        return this.mAlarmsMutableLiveData;
    }
    public MAlarm getNextCloneAlarm() {
        int nextAlarmIndex = this.getNextAlarmIndex();
        if (nextAlarmIndex != -1) { return this.getMAlarms().get(nextAlarmIndex).schedulerNextAlarm(); }
        else{ return null; }
    }
    public int getNextAlarmIndex() {
        int index = -1;
        long minTime = Long.MAX_VALUE;
        for (int i = 0; i < this.getMAlarms().size(); i++) {
            MAlarm cloneAlarm = this.getMAlarms().get(i).schedulerNextAlarm();
            if (cloneAlarm.getAlarmTime() < minTime && cloneAlarm.isChecked()) {
                index = i;
                minTime = cloneAlarm.getAlarmTime();
            }
        }
        return index;
    }

    public MAlarm getAlarm(int index) { return this.getMAlarms().get(index); }
    public void removeAlarm(int index){this.getMAlarms().remove(index);}
}
