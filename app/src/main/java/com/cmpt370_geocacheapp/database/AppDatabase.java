package com.cmpt370_geocacheapp.database;

import androidx.room.*;

@Database(entities = {User.class, Geocache.class, Comment.class, RatingReview.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserDao userDao();
    public abstract GeocacheDao geocacheDao();
    public abstract CommentDao commentDao();
    public abstract RatingReviewDao reviewDao();
}
