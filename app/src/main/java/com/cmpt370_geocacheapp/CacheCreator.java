package com.cmpt370_geocacheapp;

import java.util.ArrayList;

public class CacheCreator extends User {
    private ArrayList<CacheObject> createdCaches = new ArrayList<>();
    private ArrayList<PhysicalCacheObject> createdPhysicalCaches = new ArrayList<>();

    public CacheCreator(String username, String password, int creatorID) {
        super(username, password, creatorID);

    }

    public void createCache(String name, User author, long cacheID) {
        createdCaches.add(new CacheObject(name, author, cacheID));
    }

    public void createPhysicalCache(CacheObject cache, double latitude, double longitude, int difficulty, int terrain, int size) {
        createdPhysicalCaches.add(new PhysicalCacheObject(cache, latitude, longitude, difficulty, terrain, size ) );
    }

    public void deletePhysicalCache(long cacheID) {
        int cacheIndx = -1;
        for (int i = 0; i< createdPhysicalCaches.size(); i++) {
            if (createdPhysicalCaches.get(i).getCacheID() == cacheID) {
                cacheIndx = i;
            }
        }
        if (cacheIndx != -1) {
            createdPhysicalCaches.remove(cacheIndx);
        }
    }


    public void deleteCache(long cacheID) {
        int cacheIndx = -1;
        for (int i = 0; i< createdCaches.size(); i++) {
            if (createdCaches.get(i).getCacheID() == cacheID) {
                cacheIndx = i;
            }
        }
        if (cacheIndx != -1) {
            createdCaches.remove(cacheIndx);
        }
    }

    public CacheObject getCache(long cacheID) {
        int indx = -1;
        for (int i = 0; i < createdCaches.size(); i++) {
            if (createdCaches.get(i).getCacheID() == cacheID) {
                indx = i;
            }
        }
        if (indx != -1) {
            return createdCaches.get(indx);
        }
        return null;
    }

    public static void main(String[] args) {
    CacheCreator creator = new CacheCreator("Hella", "thor",201);
    creator.createCache("cache2", creator, 22);
    creator.createPhysicalCache(creator.getCache(22),14.02,16.003,5,1, 3);

    int totalTests = 0;
    int successfulTests = 0;

    totalTests++;
    successfulTests++;
    if (creator.getCache(22) == null) {
        System.out.println("Did not create cache or did not retrieve cache.");
        successfulTests--;
    }

        creator.deletePhysicalCache(22);

        totalTests++;
        successfulTests++;
        if (creator.getCache(22) == null) {
            System.out.println("Cache was deleted even though only the physical cache should be deleted.");
            successfulTests--;
        }

        creator.deleteCache(22);

        totalTests++;
        successfulTests++;
        if (creator.getCache(22) != null) {
            System.out.println("Cache was not deleted.");
            successfulTests--;
        }

        System.out.println(successfulTests + " out of " + totalTests + " were completed.");
    }
}

