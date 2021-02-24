package com.onandon.moca.domain.model;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.onandon.moca.domain.model.database.AlarmDao;
import com.onandon.moca.domain.model.database.AlarmRoomDatabase;
import com.onandon.moca.domain.model.database.entity.Alarm;

import java.util.List;

public class AlarmRepository {

    // Association
    private AlarmDao mAlarmDao;
    private LiveData<List<Alarm>> alarms;

    // Constructor
    public AlarmRepository(Application application) {
        AlarmRoomDatabase db = AlarmRoomDatabase.getDatabase(application);
        this.mAlarmDao = db.alarmDao();
        this.alarms = this.mAlarmDao.getIndexedAlarms();
    }

    public LiveData<List<Alarm>> getAlarms() {
        return alarms;
    }
    public void insert(Alarm alarm) { AlarmRoomDatabase.databaseWriteExecutor.execute(() -> { this.mAlarmDao.insert(alarm); }); }
    public void update(Alarm...alarms) { AlarmRoomDatabase.databaseWriteExecutor.execute(() -> { this.mAlarmDao.update(alarms); }); }
    public void delete(Alarm alarm) { AlarmRoomDatabase.databaseWriteExecutor.execute(() -> { this.mAlarmDao.delete(alarm); }); }
}
