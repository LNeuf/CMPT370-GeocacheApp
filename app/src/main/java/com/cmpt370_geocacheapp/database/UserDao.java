package com.cmpt370_geocacheapp.database;

import androidx.room.*;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;

@Dao
public interface UserDao {

    static List<Comment> getComments(UserDao dao, User user) {
        Map<User, List<Comment>> res = dao.__PRIVATE_getComments(user.username);
        List<Comment> acc = new ArrayList(res.size());
        for (List<Comment> wrappedComment : res.values()) {
            acc.addAll(wrappedComment);
        }
        return acc;
    }

    static List<RatingReview> getReviews(UserDao dao, User user) {
        Map<User, List<RatingReview>> res = dao.__PRIVATE_getReviews(user.username);
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
                    "WHERE username = :username_"
    )
    Map<User, List<Comment>> __PRIVATE_getComments(String username_);

    @Query(
            "SELECT * FROM User " +
                    "JOIN RatingReview ON User.username = RatingReview.userUsername " +
                    "WHERE username = :username_ "
    )
    Map<User, List<RatingReview>> __PRIVATE_getReviews(String username_);
}
