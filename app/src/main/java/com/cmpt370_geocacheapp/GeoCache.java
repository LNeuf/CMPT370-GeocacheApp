package com.cmpt370_geocacheapp;

public class GeoCache {

    // Placeholder geocache class, only for structure setup - will be replaced
    // attributes
    private String cacheID;     // cache ID number
    private String cacheName;   // cache name
    private int cacheType;      // Container type
    private int cacheDif;       // cache difficulty
    private int terrainDif;     // terrain difficulty
    private String creator;     // creator ID
    private float latitude;     // latitude
    private float longitude;    // longitude

        /**
     * Constructor for a new cache
     */
    public GeoCache(String cacheName, int cacheType, int cacheDif, int terrainDif, String creator, float latitude, float longitude) {
        this.cacheName = cacheName;
        this.cacheType = cacheType;
        this.cacheDif = cacheDif;
        this.terrainDif = terrainDif;
        this.creator = creator;
        this.latitude = latitude;
        this.longitude = longitude;
        // TODO: Placeholder for cacheID
        this.cacheID = cacheName+cacheDif+cacheType+terrainDif;

    }

    public void setCacheID(String cacheID) {
        this.cacheID = cacheID;
    }

    public void setCacheName(String cacheName) {
        this.cacheName = cacheName;
    }

    public void setCacheType(int cacheType) {
        this.cacheType = cacheType;
    }

    public void setCacheDif(int cacheDif) {
        this.cacheDif = cacheDif;
    }

    public void setTerrainDif(int terrainDif) {
        this.terrainDif = terrainDif;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public String getCacheID() {
        return cacheID;
    }

    public String getCacheName() {
        return cacheName;
    }

    public int getCacheType() {
        return cacheType;
    }

    public int getCacheDif() {
        return cacheDif;
    }

    public int getTerrainDif() {
        return terrainDif;
    }

    public String getCreator() {
        return creator;
    }

    public float getLatitude() {
        return latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    @Override
    public String toString() {
        return  "["+cacheName + "]:" +
                " Type:" + cacheType +
                ", Difficulty:" + cacheDif +
                ", Terrain:" + terrainDif +
                ", Created By:'" + creator +
                ", Lat:" + latitude +
                ", Long:" + longitude;
    }
}
