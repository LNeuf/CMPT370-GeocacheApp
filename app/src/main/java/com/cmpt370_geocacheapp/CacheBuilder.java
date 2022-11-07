package com.cmpt370_geocacheapp;

public interface CacheBuilder {
    // This interface is to implement the builder design pattern so that it can be expanded to virtual caches
    // if time permits
    void buildCache(String name, User author, long cacheID);
}
