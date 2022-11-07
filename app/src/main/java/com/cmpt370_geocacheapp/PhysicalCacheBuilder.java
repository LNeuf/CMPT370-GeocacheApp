package com.cmpt370_geocacheapp;

public class PhysicalCacheBuilder implements CacheBuilder {
    private CacheObject cache;

    final private CacheDatabase database;

    public PhysicalCacheBuilder(CacheDatabase databaseToLink) {
        this.database = databaseToLink;
    }

    public void buildCache(String name, User author, long cacheID) {
         this.cache = new CacheObject(name, author, cacheID);
    }
    public void buildPhysicalCache(double latitude, double longitude, String difficulty, String terrain) {
        this.database.addPhysicalCache(new PhysicalCacheObject(this.cache, latitude, longitude, difficulty,terrain));
    }

    public static void main(String[] args) {
        CacheDatabase database = new CacheDatabase();
        PhysicalCacheBuilder builder = new PhysicalCacheBuilder(database);
        User user = new User("Jeff","chips800",300);
        builder.buildCache("Cache1",user, 300);
        builder.buildPhysicalCache(19.003,0.001,"Easy", "Mountainous");
        int totalTests = 0;
        int successfulTests = 0;

        if (database.getPhysicalCache(300) == null) {
            successfulTests--;
            System.out.println("Did not build a physical cache.");
        }
        successfulTests++;
        totalTests++;

        System.out.println(successfulTests + " out of " + totalTests + " completed.");
    }
}
