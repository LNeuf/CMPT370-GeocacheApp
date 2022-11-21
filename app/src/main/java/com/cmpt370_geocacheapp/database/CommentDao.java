package com.cmpt370_geocacheapp.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface CommentDao {
    @Query("SELECT * FROM Comment")
    List<Comment> getAll();

    @Insert
    void insertAll(Comment... comments);

    @Delete
    void deleteAll(Comment... comments);

    @Update
    void updateAll(Comment... comments);
}
