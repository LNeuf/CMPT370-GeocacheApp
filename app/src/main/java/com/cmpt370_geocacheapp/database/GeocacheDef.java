package com.cmpt370_geocacheapp.database;

import android.provider.BaseColumns;

/* define the Geocaches table */
public class GeocacheDef implements BaseColumns {
    public static final String TABLE_NAME = "geocaches";

    public static final String AUTHOR = "author";
    public static final String LATITUDE = "latitude";
    public static final String LONGITUDE = "longitude";

    protected static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                    _ID + " INTEGER PRIMARY KEY," +
                    AUTHOR      + " TEXT," +
                    LATITUDE    + " REAL," +
                    LONGITUDE   + " REAL)";

}