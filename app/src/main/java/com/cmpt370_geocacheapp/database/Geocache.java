package com.cmpt370_geocacheapp.database;

import androidx.room.*;

@Entity
public class Geocache {

    @PrimaryKey
    public long id;

    public String name;

    public String author;

    public String creationDate;

    public float latitude;

    public float longitude;

}
