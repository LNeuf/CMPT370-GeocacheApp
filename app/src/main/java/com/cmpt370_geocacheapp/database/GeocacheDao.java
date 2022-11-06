package com.cmpt370_geocacheapp.database;

import androidx.room.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Dao
public interface GeocacheDao {
    @Insert
    void insertAll(Geocache... caches);

    @Delete
    void deleteAll(Geocache... caches);

    @Update
    void updateAll(Geocache... caches);

    @Query("SELECT * FROM Geocache")
    List<Geocache> getAll();

    @Query(
            "SELECT * FROM Geocache " +
                    "JOIN User ON Geocache.id = :geocacheId AND " +
                    "Geocache.userUsername = User.username"
    )
    User getUser(long geocacheId);

    @Query(
            "SELECT * FROM Geocache " +
                    "JOIN Comment ON Geocache.id = Comment.geocacheId " +
                    "AND Geocache.id = :geocacheId_"
    )
    List<Comment> getComments(long geocacheId_);

    @Query(
            "SELECT * FROM Geocache " +
                    "JOIN RatingReview ON Geocache.id = RatingReview.geocacheId " +
                    "AND Geocache.id = :geocacheId_"
    )
    List<RatingReview> getReviews(long geocacheId_);
}

