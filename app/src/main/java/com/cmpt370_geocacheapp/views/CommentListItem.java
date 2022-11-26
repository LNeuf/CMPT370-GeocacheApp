package com.cmpt370_geocacheapp.views;

public class CommentListItem {
    private final String author;
    private final String contents;
    private final int rating;
    private final long cacheID;

    /**
     * An object for displaying a cache's comment in a ListView
     * @param author - The comments author
     * @param contents - The Comment contents
     * @param rating - The comments rating
     * @param id - The comments ID
     */
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
