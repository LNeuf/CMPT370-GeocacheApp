package com.cmpt370_geocacheapp.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface PictureDao {
    @Insert
    void insertAll(Picture... pictures);

    @Delete
    void deleteAll(Picture... pictures);

    @Update
    void updateAll(Picture... pictures);

    @Query(
            "SELECT pictureBlob FROM Picture WHERE " +
                    "geocacheId == :cacheID"

    )
    byte[] getByCacheID(long cacheID);

}

