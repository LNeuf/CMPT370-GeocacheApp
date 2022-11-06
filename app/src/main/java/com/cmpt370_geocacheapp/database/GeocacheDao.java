package com.cmpt370_geocacheapp.database;

import androidx.room.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Dao
public interface GeocacheDao {

    static List<Comment> getComments(GeocacheDao dao, Geocache geocache) {
        Map<Geocache, List<Comment>> res = dao.__PRIVATE_getComments(geocache.id);
        List<Comment> acc = new ArrayList(res.size());
        for (List<Comment> wrappedComment : res.values()) {
            acc.addAll(wrappedComment);
        }
        return acc;
    }

    static List<RatingReview> getReviews(GeocacheDao dao, Geocache geocache) {
        Map<Geocache, List<RatingReview>> res = dao.__PRIVATE_getReviews(geocache.id);
        List<RatingReview> acc = new ArrayList(res.size());
        for (List<RatingReview> wrappedRatingReview : res.values()) {
            acc.addAll(wrappedRatingReview);
        }
        return acc;
    }

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
                    "JOIN Comment ON Geocache.id = Comment.geocacheId " +
                    "WHERE Geocache.id = :geocacheId_"
    )
    Map<Geocache, List<Comment>> __PRIVATE_getComments(long geocacheId_);

    @Query(
            "SELECT * FROM Geocache " +
                    "JOIN RatingReview ON Geocache.id = RatingReview.geocacheId " +
                    "WHERE Geocache.id = :geocacheId_"
    )
    Map<Geocache, List<RatingReview>> __PRIVATE_getReviews(long geocacheId_);
}

