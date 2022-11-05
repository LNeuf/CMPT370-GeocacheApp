package com.cmpt370_geocacheapp.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface RatingReviewDao {
    @Query("SELECT * FROM RatingReview")
    List<RatingReview> getAll();

    @Insert
    void insertAll(RatingReview... reviews);

    @Delete
    void deleteAll(RatingReview... reviews);
}