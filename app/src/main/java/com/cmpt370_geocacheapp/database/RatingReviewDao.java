package com.cmpt370_geocacheapp.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;
import java.util.Map;

@Dao
public interface RatingReviewDao {
    @Insert
    void insertAll(RatingReview... reviews);

    @Delete
    void deleteAll(RatingReview... reviews);

    @Update
    void updateAll(RatingReview... reviews);

    @Query("SELECT * FROM RatingReview")
    List<RatingReview> getAll();

    @Query(
            "SELECT * FROM RatingReview WHERE " +
                    "geocacheId == :cacheID"

    )
    List<RatingReview> getByCacheID(long cacheID);
}
