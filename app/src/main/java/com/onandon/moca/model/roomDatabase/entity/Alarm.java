package com.onandon.moca.model.roomDatabase.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "alarm_table")
public class Alarm {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @NonNull
    @ColumnInfo(name = "index")
    private int index;

    @NonNull
    @ColumnInfo(name = "alarm")
    private MAlarm mAlarm;

    public Alarm(MAlarm mAlarm, int index) {
        this.mAlarm = mAlarm;
        this.index = index;
    }

    public MAlarm getMAlarm() { return mAlarm; }
    public void setMAlarm(MAlarm mAlarm) { this.mAlarm = mAlarm; }
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getIndex() { return index; }
    public void setIndex(int index) { this.index = index; }
}
