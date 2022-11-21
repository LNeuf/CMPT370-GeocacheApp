package com.cmpt370_geocacheapp.model;

import java.util.Date;
import java.util.ArrayList;

public class CacheObject {
    private final String name;
    private final User author;
    private final long cacheID;
    private final String creationDate;
    private String dateLastAccessed;
    private String image;
    private String cacheDescription;


    final private ArrayList<CacheComment> commentList = new ArrayList<>();

    final private ArrayList<CacheReview> reviewList = new ArrayList<>();

    public CacheObject(String name, User author, long cacheID) {
        this.cacheID = cacheID;
        this.author = author;
        this.name = name;
        Date date = new Date();
        this.creationDate = date.toString();
    }
    public CacheObject(String name, User author, long cacheID, String image) { // over loaded if there is an image
        this.cacheID = cacheID;
        this.author = author;
        this.name = name;
        Date date = new Date();
        this.creationDate = date.toString();
        this.image = image;
    }
    public CacheObject(String name, User author, long cacheID, String image, String cacheDescription) { // over loaded if there is an image and description
        this.cacheID = cacheID;
        this.author = author;
        this.name = name;
        Date date = new Date();
        this.creationDate = date.toString();
        this.image = image;
        this.cacheDescription = cacheDescription;
    }

    public CacheObject(String name, User author, String cacheDescription, long cacheID) { // over loaded if there is a description
        this.cacheID = cacheID;
        this.author = author;
        this.name = name;
        Date date = new Date();
        this.creationDate = date.toString();
        this.cacheDescription = cacheDescription;
    }

    public String getCacheDescription() {
        return this.cacheDescription;
    }

    public void setCacheDescription(String cacheDescription) {
        this.cacheDescription = cacheDescription;
    }

    public String getImage() {
        return this.image;
    }

    public void setImage(String image_path) {
        this.image = image_path;
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

    public double getAverageReviews() {
        double rating = 0;
        for (CacheReview cacheReview : reviewList) {
            rating += cacheReview.getRating();
        }
        return rating / reviewList.size();
    }
}

