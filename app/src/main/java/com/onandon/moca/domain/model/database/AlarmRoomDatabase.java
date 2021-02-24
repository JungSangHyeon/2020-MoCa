package com.onandon.moca.domain.model.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.onandon.moca.domain.model.database.entity.Alarm;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Alarm.class}, version = 3, exportSchema = false)
public abstract class AlarmRoomDatabase extends RoomDatabase {

    // Constant
    private static final int NUMBER_OF_THREADS = 4;
    private static volatile AlarmRoomDatabase INSTANCE;
    public static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static AlarmRoomDatabase getDatabase(final Context context){
        if(INSTANCE == null){
            synchronized (AlarmRoomDatabase.class){
                if(INSTANCE==null){
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), AlarmRoomDatabase.class, "word_database")
                            .fallbackToDestructiveMigration() // 버전 바꾸면 리셋
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    public abstract AlarmDao alarmDao();
}
