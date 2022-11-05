package com.cmpt370_geocacheapp.database;

import androidx.room.*;

@Entity
public class Geocache {

    @PrimaryKey
    public int id;

    @ColumnInfo(name = "latitude")
    public float latitude;

    @ColumnInfo(name = "longitude")
    public float longitude;
}
