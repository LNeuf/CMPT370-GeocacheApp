package com.cmpt370_geocacheapp.database;

import androidx.room.*;

import java.util.List;

@Dao
public interface GeocacheDao {
    @Query("SELECT * FROM Geocache")
    List<Geocache> getAll();

    @Query("SELECT * FROM geocache WHERE id IN (:cacheIds)")
    List<Geocache> getByIds(int[] cacheIds);

    @Insert
    void insertAll(Geocache... caches);

    @Delete
    void deleteAll(Geocache... cache);
}
