package com.cmpt370_geocacheapp;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ApplicationModel {
    // models facade
    private ModelFacade facade;
    // Geocache attributes
    private ArrayList<GeoCache> nearbyCacheList;
    private ArrayList<GeoCache> filteredCacheList;

    /**
     * Constructor of the applications model
     */
    public ApplicationModel() {
        this.nearbyCacheList = new ArrayList<>();
        this.filteredCacheList = new ArrayList<>();
    }

    public void setFacade(ModelFacade facade) {
        this.facade = facade;
    }

    /**
     * Method to make facade notify model subscribers whenever the model has changed
     */
    private void modelHasChanged()
    {
        this.facade.notifyModelSubscribers();
    }

    public void updateNearbyCacheList(float latitude, float longitude, float distance)
    {
        // placeholder for interacting with the database and updating the nearby cache list
        // query database and update
    }

    /**
     * Method to update the filtered cache list based on a list of predicates
     * @param filterList - The list of filters/predicates to filter the cache list by
     */
    public void updateFilteredCacheList(ArrayList<Predicate<GeoCache>> filterList)
    {
        List<GeoCache> result = this.nearbyCacheList;
        for (Predicate<GeoCache> filter : filterList)
        {
            result = result.stream().filter(filter).collect(Collectors.toList()); // filter list by each predicate
        }

        // convert filtered caches to arraylist and update
        this.filteredCacheList = (ArrayList<GeoCache>) result;
        modelHasChanged();
    }

    public static void main(String[] args)
    {
        // main for method testing
    }

}
