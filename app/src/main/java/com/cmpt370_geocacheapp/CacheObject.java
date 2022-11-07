package com.cmpt370_geocacheapp;

import java.util.Date;
import java.util.ArrayList;

public class CacheObject {
    private final String name;
    private final User author;
    private final long cacheID;
    private final String creationDate;
    private String dateLastAccessed;

    final private ArrayList<CacheComment> commentList = new ArrayList<>();

    final private ArrayList<CacheReview> reviewList = new ArrayList<>();

    public CacheObject(String name, User author, long cacheID) {
        this.cacheID = cacheID;
        this.author = author;
        this.name = name;
        Date date = new Date();
        this.creationDate = date.toString();
    }

    public String getName() {
        return this.name;
    }

    public User getAuthor() {
        return this.author;
    }

    public long getCacheID() {
        return this.cacheID;
    }

    public void cacheAccessed() {
        Date date = new Date();
        this.dateLastAccessed = date.toString();
    }

    public CacheComment getComment(long commentID) {
        int commentIndx = -1;
        for (int i = 0; i < commentList.size(); i++) {
            if (commentList.get(i).getCommentID() == commentID) {
                commentIndx = i;
            }
        }
        if (commentList.isEmpty()) {
            return null;
        }
        if (commentIndx == -1) {
            return null;
        }
        return commentList.get(commentIndx);
    }

    public void addReview(CacheReview review) {
        reviewList.add(review);
    }

    public CacheReview getReview(long reviewID) {
        int reviewIndx = -1;
        for (int i = 0; i < reviewList.size(); i++) {
            if (reviewList.get(i).getCommentID() == reviewID) {
                reviewIndx = i;
            }
        }
        if (reviewList.isEmpty()) {
            return null;
        }
        if (reviewIndx == -1) {
            return null;
        }
        return reviewList.get(reviewIndx);
    }

    public void addComment(CacheComment commentToAdd) {
        commentList.add(commentToAdd);
    }

    public ArrayList<CacheComment> getCommentList() {
        return this.commentList;
    }

    public String getCreationDate() {
        return this.creationDate;
    }

    public String getDateLastAccessed() {
        return this.dateLastAccessed;
    }
}

