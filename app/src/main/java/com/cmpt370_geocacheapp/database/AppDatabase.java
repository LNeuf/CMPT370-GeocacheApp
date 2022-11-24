package com.cmpt370_geocacheapp.database;

import android.content.Context;

import androidx.room.*;

@Database(entities = {User.class, Geocache.class, Comment.class, RatingReview.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    private static volatile AppDatabase dbInstance;

    static final String DATABASE_NAME = "cache_db";

    public abstract UserDao userDao();
    public abstract GeocacheDao geocacheDao();
    public abstract CommentDao commentDao();
    public abstract RatingReviewDao reviewDao();

    public static AppDatabase getInstance(Context context) {
        if (dbInstance == null) {
            synchronized (AppDatabase.class) {
                if (dbInstance == null) {dbInstance = Room.databaseBuilder(context.getApplicationContext(),AppDatabase.class, DATABASE_NAME).createFromAsset(DATABASE_NAME).allowMainThreadQueries().build();
                    // TODO: copying database uses main thread still - should probably use a separate thread to prevent UI locking up
                }
            }
        }
        return dbInstance;
    }


}
