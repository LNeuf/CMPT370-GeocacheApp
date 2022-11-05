package com.cmpt370_geocacheapp;

import java.util.Date;

public class CacheComment {
    private String commentBody;
    final private User author;
    final private long commentID;
    final private String creationDate;
    private String lastEditDate;
    private boolean wasEdited;

    public CacheComment(String commentBody, User author, long commentID) {
        Date date = new Date();
        this.commentBody = commentBody;
        this.author = author;
        this.commentID = commentID;
        this.creationDate = date.toString();
    }

    public void replaceCommentBody(String commentBody) {
        Date date = new Date();
        this.commentBody = commentBody;
        this.wasEdited = true;
        this.lastEditDate = date.toString();
    }

    public String getCommentBody() {
        return this.commentBody;
    }

    public long getCommentID() {
        return this.commentID;
    }

    public User getAuthor() {
        return this.author;
    }

    public String getCreationDate() {
        return this.creationDate;
    }

    public String getLastEditDate() {
        if (this.wasEdited) {
            return this.lastEditDate;
        }
        else {
            return this.creationDate;
        }
    }


    public static void main(String[] args) {
        //TODO: Testing for CacheComment
    }
}
