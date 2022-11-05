package com.cmpt370_geocacheapp.database;

import androidx.room.*;

import java.util.List;

@Dao
public interface UserDao {
    @Query("SELECT * FROM User")
    List<User> getAll();

    @Insert
    void insertAll(User... users);

    @Delete
    void deleteAll(User... users);
}
