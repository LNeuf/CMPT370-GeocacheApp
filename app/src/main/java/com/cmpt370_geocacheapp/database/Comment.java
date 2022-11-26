package com.cmpt370_geocacheapp.database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Objects;
import java.util.stream.Stream;

@Entity
public class Comment {
    @PrimaryKey(autoGenerate = true)
    public long id;

    public String contents;

    public String userUsername;

    public long geocacheId; // The id of the associated cache
}
