package com.cmpt370_geocacheapp.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

// android studio docs recommend you create/upgrade the database through this
// 'SQLiteOpenHelper' api
public class DbHelper extends SQLiteOpenHelper {
    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(GeocacheDef.SQL_CREATE_ENTRIES);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO do nothing for now
        db.execSQL(GeocacheDef.SQL_CREATE_ENTRIES);
    }

    // If you change the database schema, you must increment the database version
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Database.db";
}

