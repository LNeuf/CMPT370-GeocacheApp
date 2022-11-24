package com.cmpt370_geocacheapp.model;

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

    public User getAuthorObject() {
        return this.author;
    }

    public String getAuthor() {
        return this.author.getUsername();
    }

    public String getCreationDate() {
        return this.creationDate;
    }

    public String getLastEditDate() {
        if (this.wasEdited) {
            return this.lastEditDate;
        } else {
            return this.creationDate;
        }
    }


    public static void main(String[] args) {
        User user = new User("Naomi", "peanutbutter345", 123);
        CacheComment comment = new CacheComment("Hello world!", user, 333);
        String creation = comment.getCreationDate();
        User authorObject = comment.getAuthorObject();
        String author = "Naomi";
        int commentID = 333;
        int userID = 123;


        int totalTests = 0;
        int completedTests = 0;
        if (authorObject != user) {
            completedTests--;
            System.out.println("The user that is the author is not showing as the author.");
        }
        completedTests++;
        totalTests++;

        if (!author.equals(comment.getAuthor())) {
            completedTests--;
            System.out.println("authors name is not stored correctly.");
        }
        completedTests++;
        totalTests++;

        if (comment.getCommentID() != commentID) {
            completedTests--;
            System.out.println("Comment ID not stored properly.");
        }
        completedTests++;
        totalTests++;

        if (userID != comment.getAuthorObject().getUserID()) {
            completedTests--;
            System.out.println("User ID not retrieved properly from comment.");
        }
        completedTests++;
        totalTests++;


        if (!comment.getCommentBody().equals("Hello world!")) {
            completedTests--;
            System.out.println("Initial body paragraph is not correct.");
        }
        completedTests++;
        totalTests++;

        comment.replaceCommentBody("Hello space!");
        if (!comment.getCommentBody().equals("Hello space!")) {
            completedTests--;
            System.out.println("Updated body paragraph not correct.");
        }
        completedTests++;
        totalTests++;

        System.out.println(completedTests + " out of " + totalTests + " were successful.");


    }
}
