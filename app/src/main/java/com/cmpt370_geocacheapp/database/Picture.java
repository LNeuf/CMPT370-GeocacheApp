package com.cmpt370_geocacheapp.database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.sql.Blob;

@Entity
public class Picture {
    @PrimaryKey(autoGenerate = true)
    public long id;

    public byte[] pictureBlob;

    public long geocacheId; // the id of the associated cache

}
