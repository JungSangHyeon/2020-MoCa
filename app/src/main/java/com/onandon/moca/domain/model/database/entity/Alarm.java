package com.onandon.moca.domain.model.database.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.onandon.moca.onAndOnAsset.utility.UObjectAndByteArrayConverter;

@Entity(tableName = "alarm_table")
public class Alarm implements Cloneable {

    @NonNull
    @Override
    public Alarm clone() {
        Alarm clone = new Alarm();
        clone.setId(this.getId());
        clone.setIndex(this.getIndex());
        clone.setAlarmData(this.getAlarmData().clone());
        clone.setFold(this.isFold());
        return clone;
    }

    @PrimaryKey(autoGenerate = true)
    private int id;

    @NonNull
    @ColumnInfo(name = "index")
    private int index;

    @NonNull
    @ColumnInfo(name = "alarm", typeAffinity = ColumnInfo.BLOB)
    private byte[] alarmData;

    @NonNull
    @ColumnInfo(name = "fold")
    boolean isFold;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getIndex() { return index; }
    public void setIndex(int index) { this.index = index; }
    public MAlarmData getMAlarm() { return (MAlarmData) UObjectAndByteArrayConverter.byteArrayToObject(this.alarmData); }
    public void setMAlarm(MAlarmData mAlarmData) { this.alarmData = UObjectAndByteArrayConverter.objectToByteArray(mAlarmData); }
    public byte[] getAlarmData() {
        return alarmData;
    }
    public void setAlarmData(byte[] alarmData) {
        this.alarmData = alarmData;
    }
    public boolean isFold() {
        return isFold;
    }
    public void setFold(boolean fold) {
        isFold = fold;
    }
}
