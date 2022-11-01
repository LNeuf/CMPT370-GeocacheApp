package com.cmpt370_geocacheapp;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ApplicationModel {
    // models facade
    private ModelFacade facade;
    // Geocache attributes
    private ArrayList<GeoCache> nearbyCacheList;
    private ArrayList<GeoCache> filteredCacheList;
    private GeoCache recommendedCache;

    /**
     * Constructor of the applications model
     */
    public ApplicationModel() {
        this.nearbyCacheList = new ArrayList<>();
        this.filteredCacheList = new ArrayList<>();
    }

    public void setNearbyCacheList(ArrayList<GeoCache> nearbyCacheList) {
        this.nearbyCacheList = nearbyCacheList;
    }

    public void setFilteredCacheList(ArrayList<GeoCache> filteredCacheList) {
        this.filteredCacheList = filteredCacheList;
    }

    public ArrayList<GeoCache> getNearbyCacheList() {
        return nearbyCacheList;
    }

    public ArrayList<GeoCache> getFilteredCacheList() {
        return filteredCacheList;
    }

    public void setFacade(ModelFacade facade) {
        this.facade = facade;
    }

    /**
     * Method to make facade notify model subscribers whenever the model has changed
     */
    private void modelHasChanged() {
        this.facade.notifyModelSubscribers();
    }

    public void updateNearbyCacheList(float latitude, float longitude, float distance) {
        // TODO: query database and update nearby cache list based on location and distance
        // clear recommended cache
        this.recommendedCache = null;
        if (this.facade != null)
            modelHasChanged();
    }

    /**
     * Method to update the filtered cache list based on a list of predicates
     *
     * @param filterList - The list of filters/predicates to filter the cache list by
     */
    public void updateFilteredCacheList(ArrayList<Predicate<GeoCache>> filterList) {
        List<GeoCache> result = this.nearbyCacheList; // gets the current list of nearby caches
        for (Predicate<GeoCache> filter : filterList) {
            result = result.stream().filter(filter).collect(Collectors.toList()); // apply each filter
        }

        // convert filtered caches to arraylist and update
        this.filteredCacheList = (ArrayList<GeoCache>) result;
        if (this.facade != null)
            modelHasChanged();
    }

    /**
     * Sets a randomly selected nearby cache that meets filter criteria to be recommended
     *
     * @param filterList - the filter criteria to meet
     * @return - Returns a geocache from the nearby cache list that meets criteria, otherwise returns null if no caches meet criteria
     */
    public GeoCache getRecommendedCache(ArrayList<Predicate<GeoCache>> filterList) {
        List<GeoCache> result = this.nearbyCacheList; // gets the current list of nearby caches
        for (Predicate<GeoCache> filter : filterList) {
            result = result.stream().filter(filter).collect(Collectors.toList()); // apply each filter
        }

        // Return random cache that meets criteria, otherwise return null if no caches meet criteria
        if (result.size() > 1) {
            Random rand = new Random();
            int randomIndex = rand.nextInt(result.size());
            return result.get(randomIndex);
        } else if (result.size() == 1)
            return result.get(0);
        else
            return null;

    }

    public GeoCache SearchByID(String cacheID) {
        // TODO: search whole DB for specific cache ID
        return null;
    }
}
