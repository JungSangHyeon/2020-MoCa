package com.onandon.moca.model.roomDatabase;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.onandon.moca.model.roomDatabase.entity.Alarm;

import java.util.List;

@Dao
public interface AlarmDao {

    @Insert
    void insert(Alarm alarm);

    @Update
    void update(Alarm alarm);

    @Delete
    void delete(Alarm alarm);

    @Query("DELETE FROM alarm_table")
    void deleteAll();

    @Query("SELECT * FROM alarm_table ORDER BY `index`")
    LiveData<List<Alarm>> getIndexedAlarms();
}
