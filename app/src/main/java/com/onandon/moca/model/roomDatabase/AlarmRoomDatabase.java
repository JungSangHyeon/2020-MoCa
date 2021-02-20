package com.onandon.moca.model.roomDatabase;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.onandon.moca.model.roomDatabase.entity.Alarm;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Alarm.class}, version = 1, exportSchema = false)
public abstract class AlarmRoomDatabase extends RoomDatabase {

    public abstract AlarmDao alarmDao();

    private static volatile AlarmRoomDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static AlarmRoomDatabase getDatabase(final Context context){
        if(INSTANCE == null){
            synchronized (AlarmRoomDatabase.class){
                if(INSTANCE==null){
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), AlarmRoomDatabase.class, "word_database")
                            .addCallback(sRoomDatabaseCallback)
                            .fallbackToDestructiveMigration() // 버전 바꾸면 리셋
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static Callback sRoomDatabaseCallback = new Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

            // If you want to keep data through app restarts,
            // comment out the following block
//            databaseWriteExecutor.execute(() -> {
//                // Populate the database in the background.
//                // If you want to start with more words, just add them.
//                AlarmDao dao = INSTANCE.alarmDao();
//                dao.deleteAll();
//
//                Alarm alarm = new Alarm("Hello");
//                dao.insert(alarm);
//                alarm = new Alarm("World");
//                dao.insert(alarm);
//            });
        }
    };

}
