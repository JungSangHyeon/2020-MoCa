package com.onandon.moca.model.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class User implements Serializable {

    public User(int uid, byte[] mAlarms) {
        this.uid=uid;
        this.mAlarms = mAlarms;
    }

    @PrimaryKey
    public int uid;

    @ColumnInfo(name = "mAlarms", typeAffinity = ColumnInfo.BLOB)
    public byte[] mAlarms;
}
