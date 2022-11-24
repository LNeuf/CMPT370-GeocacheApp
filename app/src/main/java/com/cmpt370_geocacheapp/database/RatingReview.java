package com.cmpt370_geocacheapp.database;

import androidx.room.*;

@Entity
public class RatingReview {
    @PrimaryKey(autoGenerate = true)
    public long id;

    public int rating;

    public String contents;

    public String userUsername;

    public long geocacheId; // the id of the associated cache

}
