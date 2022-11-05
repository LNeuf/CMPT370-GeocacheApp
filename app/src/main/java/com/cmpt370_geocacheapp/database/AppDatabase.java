package com.cmpt370_geocacheapp.database;

import androidx.room.*;

@Database(entities = {Geocache.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract GeocacheDao geocacheDao();
}
