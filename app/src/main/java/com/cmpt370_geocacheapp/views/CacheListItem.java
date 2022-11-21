package com.cmpt370_geocacheapp.views;

public class CacheListItem {
    private String cacheID;
    private String cacheName;
    private String cacheDescription;
    private String time;
    private String cacheDistance;
    private double latitude;
    private double longitude;


    public CacheListItem(String name, String description, String time, String id, String distance, double lat, double lon) {
        this.cacheName = name;
        this.cacheDescription = description;
        this.time = time;
        this.cacheID = id;
        this.cacheDistance = distance;
        this.latitude = lat;
        this.longitude = lon;

    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getCacheName() {
        return this.cacheName;
    }

    public String getCacheDescription() {
        return cacheDescription;
    }

    public String getTime() {
        return this.time;
    }

    public String getID() {
        return this.cacheID;
    }

    public String getCacheDistance() {
        return this.cacheDistance;
    }

    public void setCacheDistance(String dist) {this.cacheDistance = dist;}
}
