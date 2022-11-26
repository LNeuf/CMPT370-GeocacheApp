package com.cmpt370_geocacheapp.model;

public class PhysicalCacheBuilder implements CacheBuilder {
    private CacheObject cache;

    final private CacheDatabase database;

    public PhysicalCacheBuilder(CacheDatabase databaseToLink) {
        this.database = databaseToLink;
    }

    /**
     * Creates a new cache
     * @param name - The cache name
     * @param author - The cache author
     * @param cacheID - The cache ID
     */
    public void buildCache(String name, User author, long cacheID) {
        this.cache = new CacheObject(name, author, cacheID);
    }

    /**
     * Creates a new cache and adds to DB
     * @param latitude - Latitude
     * @param longitude - Longitude
     * @param cacheDif - Difficulty
     * @param terrainDif - Terrain Difficulty
     * @param cacheSize - Cache size
     */
    public void buildPhysicalCache(double latitude, double longitude, int cacheDif, int terrainDif, int cacheSize) {
        this.database.addPhysicalCache(new PhysicalCacheObject(this.cache, latitude, longitude, cacheDif, terrainDif, cacheSize));
    }

    public static void main(String[] args) {
        CacheDatabase database = new CacheDatabase();
        PhysicalCacheBuilder builder = new PhysicalCacheBuilder(database);
        User user = new User("Jeff", "chips800", 300);
        builder.buildCache("Cache1", user, 300);
        builder.buildPhysicalCache(19.003, 0.001, 1, 5, 3);
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
