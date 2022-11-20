package com.cmpt370_geocacheapp;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.function.Predicate;

public class ApplicationController {
    // MVC attributes
    private ApplicationModel model;
    private InteractionModel iModel;


    /**
     * Empty constructor
     */
    public ApplicationController() {
        // do nothing
    }

    /**
     * Sets the controller's application model
     *
     * @param model - The controllers model
     */
    public void setModel(ApplicationModel model) {
        this.model = model;
    }

    /**
     * Sets the controller iModel
     *
     * @param iModel - The controllers iModel
     */
    public void setInteractionModel(InteractionModel iModel) {
        this.iModel = iModel;
    }

    public void setSelectedCache(PhysicalCacheObject selectedCache) {
        iModel.setCurrentlySelectedCache(selectedCache);
    }

    /**
     * Creates a new cache
     */
    public void createCache(String cacheName, User cacheCreator, float latitude, float longitude, int cacheDifficulty, int terrainDifficulty, int cacheSize) {
        PhysicalCacheObject newCache = model.createNewCache(cacheName, cacheCreator, latitude, longitude, cacheDifficulty, terrainDifficulty, cacheSize);
        iModel.setCurrentlySelectedCache(newCache);
    }

    /**
     * Applies the specified filters, adn filters by distance if a valid loaction exists
     *
     * @param filters - the cache filters to apply
     */
    public void applyFilters(ArrayList<Predicate<PhysicalCacheObject>> filters) {
        iModel.clearFilters();
        iModel.updateFilters(filters);
        model.updateFilteredCacheList(filters);
        if (iModel.getCurrentLocation() != null && iModel.getMaxDistance() != -1) {
            model.filterCachesByDistance(iModel.getCurrentLocation().getLatitude(), iModel.getCurrentLocation().getLongitude(), iModel.getMaxDistance());
        }
    }

    /**
     * Sets the current max distance to show caches out to
     *
     * @param distance - the distance in meters
     */
    public void setMaxDistance(int distance) {
        iModel.setMaxDistance(distance);
    }

    /**
     * Gets a recommended cache and sets it to selected
     *
     * @param filters  - the criteria filters to apply
     * @param distance - the max distance of the recommended cache
     * @return - returns true if successful and got a cache, false otherwise
     */
    public boolean getRecommendedCache(ArrayList<Predicate<PhysicalCacheObject>> filters, int distance) {
        // needs to apply filters as well so that the recommended cache is actually shown on the map and in the list
        model.updateFilteredCacheList(filters);
        if (iModel.getCurrentLocation() != null) {
            model.filterCachesByDistance(iModel.getCurrentLocation().getLatitude(), iModel.getCurrentLocation().getLongitude(), distance);
        }

        // get a recommended cache from the available caches
        PhysicalCacheObject recommendedCache = model.getRecommendedCache(filters,
                new LatLng(iModel.getCurrentLocation().getLatitude(), iModel.getCurrentLocation().getLongitude()), distance);
        if (recommendedCache != null) {
            iModel.setCurrentlySelectedCache(recommendedCache);
            return true;
        }
        return false;
    }

    /**
     * Performs a search of cache Names
     *
     * @param searchInput - the search input
     * @return - true if atleast one cache was found, false otherwise
     */
    public boolean searchByName(String searchInput) {
        return model.searchByCacheName(searchInput);
    }

    /**
     * Performs a search of cache author names
     *
     * @param searchInput - the search input
     * @return - true if atleast one cache was found, false otherwise
     */
    public boolean searchByAuthorName(String searchInput) {
        return model.searchByAuthorName(searchInput);
    }

    /**
     * Performs a search of cache id's
     *
     * @param searchID - the cache id to search for
     * @return - true if a cache was found, false otherwise
     */
    public boolean searchByCacheID(long searchID) {
        PhysicalCacheObject foundCache = model.searchByCacheID(searchID);
        if (foundCache != null) {
            model.updateFilteredCacheList(new ArrayList<>()); // clear filters to show all loaded caches
            iModel.setCurrentlySelectedCache(foundCache);
            return true;
        } else {
            return false;
        }

    }

    /**
     * Creates a new cache rating/comment
     *
     * @param username          - Name of user who created
     * @param contents          - comment contents
     * @param rating            - rating out of five
     * @param currentGeocacheID - id of associated cache
     */
    public void createRating(String username, String contents, int rating, long currentGeocacheID) {
        model.createNewRating(username, contents, rating, currentGeocacheID);
    }
}
