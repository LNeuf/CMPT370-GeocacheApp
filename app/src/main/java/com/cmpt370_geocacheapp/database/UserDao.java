package com.cmpt370_geocacheapp.database;

import androidx.room.*;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;

@Dao
public interface UserDao {

    @Insert
    void insertAll(User... users);

    @Delete
    void deleteAll(User... users);

    @Update
    void updateAll(User... users);

    @Query("SELECT * FROM User")
    List<User> getAll();

    @Query("SELECT * FROM User WHERE User.username = :username_")
    User getByUsername(String username_);

    @Query(
            "SELECT * FROM User " +
                    "JOIN Comment ON User.username = Comment.userUsername " +
                    "AND User.username = :username_"
    )
    List<Comment> getComments(String username_);

    @Query(
            "SELECT * FROM User " +
                    "JOIN RatingReview ON User.username = RatingReview.userUsername " +
                    "AND User.username = :username_ "
    )
    List<RatingReview> getReviews(String username_);

    @Query(
            "SELECT * FROM User " +
                    "JOIN Geocache ON User.username = Geocache.userUsername " +
                    "AND User.username = :username_"
    )
    List<Geocache> getGeocaches(String username_);
}
