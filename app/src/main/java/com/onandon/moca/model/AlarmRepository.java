package com.onandon.moca.model;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.onandon.moca.model.roomDatabase.AlarmDao;
import com.onandon.moca.model.roomDatabase.AlarmRoomDatabase;
import com.onandon.moca.model.roomDatabase.entity.Alarm;

import java.util.List;

public class AlarmRepository {

    private AlarmDao mAlarmDao;
    private LiveData<List<Alarm>> alarms;

    // Note that in order to unit test the WordRepository, you have to remove the Application
    // dependency. This adds complexity and much more code, and this sample is not about testing.
    // See the BasicSample in the android-architecture-components repository at
    // https://github.com/googlesamples
    AlarmRepository(Application application) {
        AlarmRoomDatabase db = AlarmRoomDatabase.getDatabase(application);
        mAlarmDao = db.alarmDao();
        alarms = mAlarmDao.getIndexedAlarms();
    }

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    LiveData<List<Alarm>> getAlarms() {
        return alarms;
    }

    // You must call this on a non-UI thread or your app will throw an exception. Room ensures
    // that you're not doing any long running operations on the main thread, blocking the UI.
    void insert(Alarm alarm) {
        AlarmRoomDatabase.databaseWriteExecutor.execute(() -> {
            mAlarmDao.insert(alarm);
        });
    }

    void update(Alarm alarm) {
        AlarmRoomDatabase.databaseWriteExecutor.execute(() -> {
            mAlarmDao.update(alarm);
        });
    }

    void delete(Alarm alarm) {
        AlarmRoomDatabase.databaseWriteExecutor.execute(() -> {
            mAlarmDao.delete(alarm);
        });
    }
}
