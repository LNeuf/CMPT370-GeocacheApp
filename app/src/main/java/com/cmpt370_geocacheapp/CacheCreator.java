package com.cmpt370_geocacheapp;

import java.util.ArrayList;

public class CacheCreator extends User {
    ArrayList<CacheObject> createdCaches = new ArrayList<>();
    ArrayList<PhysicalCacheObject> createdPhysicalCaches = new ArrayList<>();

    public CacheCreator(String username, String password, int creatorID) {
        super(username, password, creatorID);

    }

    public void createCache(String name, String author, long cacheID) {
        createdCaches.add(new CacheObject(name, author, cacheID));
    }

    public void createPhysicalCache(CacheObject cache, double latitude, double longitude, String difficulty, String terrain) {
        createdPhysicalCaches.add(new PhysicalCacheObject(cache, latitude, longitude, difficulty, terrain ) );
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

    public static void main(String[] args) {
    //TODO: Testing for CacheCreator object

    }
}

