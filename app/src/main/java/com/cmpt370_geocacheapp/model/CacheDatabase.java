package com.cmpt370_geocacheapp.model;

import java.util.ArrayList;

public class CacheDatabase {
    final private ArrayList<PhysicalCacheObject> physicalCacheList;

    public CacheDatabase() {
        this.physicalCacheList = new ArrayList<>();
    }

    /**
     * Returns the whole cache list
     */
    public ArrayList<PhysicalCacheObject> getPhysicalCacheList() {
        return this.physicalCacheList;
    }

    /**
     * Returns a specific cache from the cache ID
     * @param cacheID -The id of the cache to return
     */
    public PhysicalCacheObject getPhysicalCache(long cacheID) {
        int indx = -1;
        for (int i = 0; i < physicalCacheList.size(); i++) {
            if (physicalCacheList.get(i).getCacheID() == cacheID) {
                indx = i;
            }
        }
        if (indx != -1) {
            return physicalCacheList.get(indx);
        }
        return null;
    }

    /**
     * Adds a new cache to the cache list
     */
    public void addPhysicalCache(PhysicalCacheObject newCache) {
        physicalCacheList.add(newCache);
    }

    /**
     * Removes a cache from teh cache list
     */
    public void removePhysicalCache(PhysicalCacheObject cache) {
        physicalCacheList.remove(cache);
    }

    /**
     * Removes a cache object from teh cache list
     */
    public void removeCacheObject(long cacheID) {
        int indx = -1;
        for (int i = 0; i < physicalCacheList.size(); i++) {
            if (physicalCacheList.get(i).getCacheID() == cacheID) {
                indx = i;
            }
        }
        if (indx != -1) {
            physicalCacheList.remove(indx);
        }
    }

    /**
     * Helper method for squaring a number
     * @param num - The number to square
     */
    private double squared(double num) {
        return num * num;
    }

    /**
     * Filters caches based on distance and position
     * @param maxDistance - The max distance a cache can be to not be filtered
     * @param currentLatitude - The position latitude
     * @param currentLongitude - The position Longitude
     */
    public ArrayList<PhysicalCacheObject> filterCacheDistance(double maxDistance, double currentLatitude, double currentLongitude) {
        ArrayList<PhysicalCacheObject> distanceFilteredCaches = new ArrayList<>();
        double lat;
        double lon;
        double distanceSquared;
        for (PhysicalCacheObject physicalCache : physicalCacheList) {
            lat = physicalCache.getCacheLatitude();
            lon = physicalCache.getCacheLongitude();

            distanceSquared = squared(lat - currentLatitude) + squared(lon - currentLongitude);

            if (distanceSquared <= squared(maxDistance)) {
                distanceFilteredCaches.add(physicalCache);
            }

        }
        return distanceFilteredCaches;
    }


    public static void main(String[] args) {
        CacheDatabase database = new CacheDatabase();
        User user = new User("Hilbert", "Json5", 99);

        CacheObject cache1Object = new CacheObject("Saskatoon Cache", user, 801);
        PhysicalCacheObject cache1 = new PhysicalCacheObject(cache1Object, 14.007, 14.003, 5, 3, 1);
        database.addPhysicalCache(cache1);

        CacheObject cache2Object = new CacheObject("Regina Cache", user, 802);
        PhysicalCacheObject cache2 = new PhysicalCacheObject(cache2Object, 15.032, 16.704, 1, 1, 2);
        database.addPhysicalCache(cache2);

        int totalTests = 0;
        int successfulTests = 0;

        totalTests++;
        successfulTests++;
        if (database.getPhysicalCache(cache1.getCacheID()) != cache1) {
            System.out.println("Cache was unable to be retrieved from the list of caches");
            successfulTests--;
        }

        totalTests++;
        successfulTests++;
        if (!database.getPhysicalCache(cache2.getCacheID()).getCacheName().equals(cache2.getCacheName())) {
            System.out.println("Cache was unable to be retrieved from list of cache or retrieving the cache name is not working");
            successfulTests--;
        }
        System.out.println(successfulTests + " tests completed out of " + totalTests);
    }
}
