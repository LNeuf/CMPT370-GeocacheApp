package com.cmpt370_geocacheapp.database;

import androidx.room.*;

@Entity
public class RatingReview {
    @PrimaryKey
    public long id;

    public int rating;

    public String contents;

    public long userId; // each review belongs to one user, one cache
    public long geocacheId;

}
