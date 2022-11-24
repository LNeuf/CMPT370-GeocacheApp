package com.cmpt370_geocacheapp.model;

public class CacheReview extends CacheComment {
    private int rating;

    public CacheReview(String commentBody, User author, long reviewID, int rating) {
        super(commentBody, author, reviewID);
        this.rating = rating;
    }

    public long getReviewID() {
        return this.getCommentID();
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public int getRating() {
        return this.rating;
    }

    public static void main(String[] args) {
        User author = new User("Peanut", "butter", 548);
        CacheReview review = new CacheReview("This is the review body!", author, 399, 5);
        int expectedRating = 5;
        String reviewBody = "This is the review body!";
        long reviewID = 399;
        int newRating = 4;
        int totalTests = 0;
        int successfulTests = 0;


        successfulTests++;
        totalTests++;
        if (reviewID != review.getReviewID()) {
            successfulTests--;
            System.out.println("The id for the review is not what was expected.");
        }

        successfulTests++;
        totalTests++;
        if (review.getAuthorObject() != author) {
            successfulTests--;
            System.out.println("The user that created the cache was not stored properly.");

        }

        successfulTests++;
        totalTests++;
        if (review.getRating() != expectedRating) {
            successfulTests--;
            System.out.println("The rating was not what was expected.");
        }

        successfulTests++;
        totalTests++;
        if (!review.getCommentBody().equals(reviewBody)) {
            successfulTests--;
            System.out.println("The comment portion of the review was not stored correctly.");
        }

        review.setRating(4);
        successfulTests++;
        totalTests++;
        if (review.getRating() != newRating) {
            successfulTests--;
            System.out.println("The rating was not what was expected, possibly it was not changed.");

        }
        System.out.println(successfulTests + " tests out of " + totalTests + " were successful.");
    }
}
