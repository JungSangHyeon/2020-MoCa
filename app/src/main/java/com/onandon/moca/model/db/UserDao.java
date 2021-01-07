package com.onandon.moca.model.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface UserDao {
    @Query("SELECT * FROM user")
    List<User> getAll();

    @Query("SELECT * FROM user WHERE uid IN (:userIds)")
    List<User> loadAllByIds(int[] userIds);

    @Query("SELECT * FROM user WHERE uid = (:userId)")
    User findById(int userId);

    @Query("UPDATE user SET mAlarms = :mAlarms WHERE uid = :uid")
    void updateMAlarms(int uid, byte[] mAlarms);

    @Insert
    void insertAll(User... users);

    @Delete
    void delete(User user);
}

