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


    public void createCache(String cacheName,User cacheCreator,float latitude,float longitude,int cacheDifficulty,int terrainDifficulty,int cacheSize) {
        PhysicalCacheObject newCache = model.createNewCache(cacheName,cacheCreator,latitude,longitude,cacheDifficulty,terrainDifficulty,cacheSize);
        iModel.setCurrentlySelectedCache(newCache);
    }

    public void applyFilters(ArrayList<Predicate<PhysicalCacheObject>> filters) {
        iModel.clearFilters();
        iModel.updateFilters(filters);
        model.updateFilteredCacheList(filters);
        if (iModel.getCurrentLocation() != null && iModel.getMaxDistance() != -1) {
            model.filterCachesByDistance(iModel.getCurrentLocation().getLatitude(), iModel.getCurrentLocation().getLongitude(), iModel.getMaxDistance());
        }
    }

    public void setMaxDistance(int distance) {
        iModel.setMaxDistance(distance);
    }


    public boolean getRecommendedCache(ArrayList<Predicate<PhysicalCacheObject>> filters, int distance) {
        // needs to apply filters as well so that the recommended cache is actually shown on the map and in the list
        model.updateFilteredCacheList(filters);
        if (iModel.getCurrentLocation() != null)
        {
            model.filterCachesByDistance(iModel.getCurrentLocation().getLatitude(), iModel.getCurrentLocation().getLongitude(), distance);
        }

        // get a recommended cache from the available caches
        PhysicalCacheObject recommendedCache = model.getRecommendedCache(filters,
                new LatLng(iModel.getCurrentLocation().getLatitude(), iModel.getCurrentLocation().getLongitude()), distance);
        if (recommendedCache != null)
        {
            iModel.setCurrentlySelectedCache(recommendedCache);
            return true;
        }
        return false;
    }

    public boolean searchByName(String searchInput) {
        return model.searchByCacheName(searchInput);
    }

    public boolean searchByAuthorName(String searchInput) {
        return model.searchByAuthorName(searchInput);
    }

    public boolean searchByCacheID(long searchID) {
        PhysicalCacheObject foundCache = model.searchByCacheID(searchID);
        if (foundCache != null)
        {
            model.updateFilteredCacheList(new ArrayList<>()); // clear filters to show all loaded caches
            iModel.setCurrentlySelectedCache(foundCache);
            return true;
        }
        else
        {
            return false;
        }

    }

    public void createRating(String username, String contents, int rating, long currentGeocacheID) {
        model.createNewRating(username, contents, rating, currentGeocacheID);
    }
}
