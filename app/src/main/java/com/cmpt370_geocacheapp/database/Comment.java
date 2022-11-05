package com.cmpt370_geocacheapp.database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Comment {
    @PrimaryKey
    public long id;

    public String contents;

    public long userId; // each comment belongs to one user, one cache
    public long geocacheId;
}
