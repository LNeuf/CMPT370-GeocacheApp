package com.cmpt370_geocacheapp.database;

import androidx.room.*;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;

@Dao
public interface UserDao {

    static List<Geocache> getCaches(UserDao dao, String username) {
        Map<User, List<Geocache>> res = dao.__PRIVATE_getGeocaches(username);
        List<Geocache> acc = new ArrayList(res.size());
        for (List<Geocache> wrappedGeocache : res.values()) {
            acc.addAll(wrappedGeocache);
        }
        return acc;
    }

    static List<Comment> getComments(UserDao dao, String username) {
        Map<User, List<Comment>> res = dao.__PRIVATE_getComments(username);
        List<Comment> acc = new ArrayList(res.size());
        for (List<Comment> wrappedComment : res.values()) {
            acc.addAll(wrappedComment);
        }
        return acc;
    }

    static List<RatingReview> getReviews(UserDao dao, String username) {
        Map<User, List<RatingReview>> res = dao.__PRIVATE_getReviews(username);
        List<RatingReview> acc = new ArrayList(res.size());
        for (List<RatingReview> wrappedRatingReview : res.values()) {
            acc.addAll(wrappedRatingReview);
        }
        return acc;
    }

    @Insert
    void insertAll(User... users);

    @Delete
    void deleteAll(User... users);

    @Update
    void updateAll(User... users);

    @Query("SELECT * FROM User")
    List<User> getAll();

    @Query(
            "SELECT * FROM User " +
                    "JOIN Comment ON User.username = Comment.userUsername " +
                    "AND User.username = :username_"
    )
    Map<User, List<Comment>> __PRIVATE_getComments(String username_);

    @Query(
            "SELECT * FROM User " +
                    "JOIN RatingReview ON User.username = RatingReview.userUsername " +
                    "AND User.username = :username_ "
    )
    Map<User, List<RatingReview>> __PRIVATE_getReviews(String username_);

    @Query(
            "SELECT * FROM User " +
                    "JOIN Geocache ON User.username = Geocache.userUsername " +
                    "AND User.username = :username_"
    )
    Map<User, List<Geocache>> __PRIVATE_getGeocaches(String username_);
}
