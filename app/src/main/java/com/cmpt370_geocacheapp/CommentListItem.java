package com.cmpt370_geocacheapp;

public class CommentListItem {
    private String author;
    private String contents;
    private int rating;
    private long cacheID;


    public CommentListItem(String author, String contents, int rating, long id) {
        this.author = author;
        this.contents = contents;
        this.rating = rating;
        this.cacheID = id;
    }

    public String getAuthor() {
        return author;
    }

    public String getContents() {
        return contents;
    }

    public int getRating() {
        return rating;
    }

    public long getCacheID() {
        return cacheID;
    }
}
