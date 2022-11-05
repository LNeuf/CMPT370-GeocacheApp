package com.cmpt370_geocacheapp.database;

import android.provider.BaseColumns;

/* define the Reviews table*/
public class ReviewDef implements BaseColumns {
    public static final String TABLE_NAME = "reviews";
    public static final String AUTHOR = "author";
    public static final String TITLE = "title";
    public static final String CONTENTS = "contents";

    protected static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    _ID + " INTEGER PRIMARY KEY," +
                    AUTHOR   + " TEXT," +
                    TITLE    + " TEXT," +
                    CONTENTS + " TEXT)";
}
