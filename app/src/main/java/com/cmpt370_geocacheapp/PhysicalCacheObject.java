package com.cmpt370_geocacheapp;

//import javax.xml.stream.events.Comment;

public class PhysicalCacheObject {
    CacheObject cache;
    private final double latitude;
    private final double longitude;
    private final int cacheDifficulty;
    private final int terrainDifficulty;
    private final int cacheSize;
    private final String[] cacheSizeNames = new String[] {"Micro","Small","Regular","Large","Other"};

    public PhysicalCacheObject(CacheObject cache, double latitude, double longitude, int cacheDifficulty, int terrainDifficulty, int cacheSize) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.cacheDifficulty = cacheDifficulty;
        this.terrainDifficulty = terrainDifficulty;
        this.cacheSize = cacheSize;
        this.cache = cache;
    }

    public String getCacheName() {
        return this.cache.getName();
    }
    public User getCacheAuthorObject() {
        return this.cache.getAuthor();
    }

    public String getCacheAuthor() {
        return this.cache.getAuthor().getUsername();
    }


    public double getCacheLatitude() {
        return this.latitude;
    }

    public double getCacheLongitude() {
        return this.longitude;
    }
    public int getCacheDifficulty() {
        return this.cacheDifficulty;
    }
    public long getCacheID() {
        return this.cache.getCacheID();
    }
    public int getTerrainDifficulty() {
        return this.terrainDifficulty;
    }
    public int getCacheSize() { return this.cacheSize; }

    public String getCacheSummary()
    {
        return String.format("%s | Dif:%d/5 | Ter:%d/5 | ID:%d", cacheSizeNames[cacheSize-1], cacheDifficulty, terrainDifficulty, cache.getCacheID());
    }

    public double[] getCacheCoordinates() {
        /*
        Returns coordinates in the form of latitude and longitude
         */
        double[] cacheCoordinates = new double[2];
        cacheCoordinates[0] = this.latitude;
        cacheCoordinates[1] = this.longitude;
        return cacheCoordinates;
    }


    public void addComment(CacheComment commentToAdd) {
        cache.addComment(commentToAdd);
    }

    public CacheComment readComment(long commentID) {
        int indx = -1;
        for (int i = 0; i < this.cache.getCommentList().size(); i++) {
            if (this.cache.getCommentList().get(i).getCommentID() == commentID) {
                indx = i;
            }
        }
        if (indx != -1) {
            return this.cache.getCommentList().get(indx);
        }
        return null;
    }

    public String getCreationDate() {
        return this.cache.getCreationDate();
    }

    public String getDateLastAccessed() {
        return this.cache.getDateLastAccessed();
    }

    public static void main(String[] args) {
        int testsPerformed = 0;
        int expectedTests = 0;

        String expectedName = "Saskatoon cache";
        String expectedAuthor = "Ashton";
        double expectedLatitude = 18.329384;
        double expectedLongitude = 14.00343;
        int expectedDifficulty = 3;
        long expectedCacheID = 1;
        int expectedTerrainDifficulty = 1;
        int expectedCacheSize = 5;

        CacheObject cache = new CacheObject("Saskatoon cache", new User("Ashton", "password", 123),1);
        PhysicalCacheObject physicalCache = new PhysicalCacheObject(cache, 18.329384,14.00343,3,1,5 );


        testsPerformed += 1;
        expectedTests += 1;
        if (!expectedName.equals(physicalCache.getCacheName())) {
            testsPerformed -= 1;
            System.out.println("getCacheName() method failed.");
        }
        testsPerformed += 1;
        expectedTests += 1;
        if (!expectedAuthor.equals(physicalCache.getCacheAuthor())) {
            testsPerformed -= 1;
            System.out.println("getCacheAuthor() method failed.");
        }
        testsPerformed += 1;
        expectedTests += 1;
        if (expectedLatitude != physicalCache.getCacheLatitude()) {
            testsPerformed -= 1;
            System.out.println("getCacheLatitude() method failed.");
        }
        testsPerformed += 1;
        expectedTests += 1;
        if (expectedLongitude != physicalCache.getCacheLongitude()) {
            testsPerformed -= 1;
            System.out.println("getCacheLongitude() method failed.");
        }
        testsPerformed += 1;
        expectedTests += 1;
        if (expectedDifficulty != physicalCache.getCacheDifficulty()) {
            testsPerformed -= 1;
            System.out.println("getCacheDifficulty() method failed.");
        }
        testsPerformed += 1;
        expectedTests += 1;
        if (expectedCacheID != cache.getCacheID()) {
            testsPerformed -= 1;
            System.out.println("getCacheID() method failed.");
        }
        testsPerformed += 1;
        expectedTests += 1;
        if (expectedTerrainDifficulty != physicalCache.getTerrainDifficulty()) {
            testsPerformed -= 1;
            System.out.println("getTerrain() method failed.");
        }
        testsPerformed += 1;
        expectedTests += 1;
        if (expectedCacheSize != physicalCache.getCacheSize()) {
            testsPerformed -= 1;
            System.out.println("getCacheSize() method failed.");
        }
        testsPerformed += 1;
        expectedTests += 1;
        if (physicalCache.getCacheLatitude() != physicalCache.getCacheCoordinates()[0]) {
            testsPerformed -= 1;
            System.out.println("getCacheCoordinates() or getCacheLatitude() method failed.");
        }
        testsPerformed += 1;
        expectedTests += 1;
        if (physicalCache.getCacheLongitude() != physicalCache.getCacheCoordinates()[1]) {
            testsPerformed -= 1;
            System.out.println("getCacheCoordinates() or getCacheLongitude() method failed.");
        }
        User author = new User("Ashton", "potato", 1);
        CacheComment comment = new CacheComment("Comment body test for a series of characters.", author, 1);
        cache.addComment(comment);
        CacheReview review = new CacheReview("This is the review's body",author,1,3);
        cache.addReview(review);
        testsPerformed += 1;
        expectedTests += 1;
        if (comment != cache.getComment(comment.getCommentID())) {
            testsPerformed -= 1;
            System.out.println("Comment not stored in comment list correctly.");
        }

        testsPerformed += 1;
        expectedTests += 1;
        if (cache.getReview(review.getReviewID()) != review) {
            testsPerformed -= 1;
            System.out.println("Review not stored in review list correctly.");
        }
        cache.addReview(new CacheReview("Review", author, 2, 5 ));

        testsPerformed += 1;
        expectedTests += 1;
        if (cache.getAverageReviews() != 4.0) {
            testsPerformed -= 1;
            System.out.println("Average reviews not what was expected.");
        }
        System.out.println(testsPerformed + " tests were performed out of " + expectedTests);
    }

    public String getCacheSizeString() {
        return cacheSizeNames[cacheDifficulty - 1];
    }
}
