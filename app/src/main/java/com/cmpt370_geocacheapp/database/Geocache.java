package com.cmpt370_geocacheapp.database;

import androidx.room.*;

@Entity
public class Geocache {

    @PrimaryKey(autoGenerate = true)
    public long id;

    public float latitude;

    public float longitude;

    public int cacheDiff;

    public int terrainDiff;

    public int cacheSize;

    public String userUsername;

    public String cacheName;

    public double cacheRating;

}
