package com.onandon.moca.model;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.onandon.moca.model.AlarmRepository;
import com.onandon.moca.model.roomDatabase.entity.Alarm;

import java.util.List;

public class AlarmViewModel extends AndroidViewModel {

    private AlarmRepository mRepository;

    private final LiveData<List<Alarm>> alarms;

    public AlarmViewModel(Application application) {
        super(application);
        mRepository = new AlarmRepository(application);
        alarms = mRepository.getAlarms();
    }

    LiveData<List<Alarm>> getAlarms() { return alarms; }

    public void insert(Alarm alarm) { mRepository.insert(alarm); }
    public void update(Alarm alarm) { mRepository.update(alarm); }
    public void delete(Alarm alarm) { mRepository.delete(alarm); }
}
