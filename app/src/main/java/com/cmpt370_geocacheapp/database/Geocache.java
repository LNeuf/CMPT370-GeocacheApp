package com.cmpt370_geocacheapp.database;

import android.webkit.GeolocationPermissions;

import androidx.room.*;

@Entity
public class Geocache {

    @PrimaryKey(autoGenerate = true)
    public long id;

    public String name;

    public String author;

    public String creationDate;

    public float latitude;

    public float longitude;

    public String userUsername; // each geocache has one user
}
