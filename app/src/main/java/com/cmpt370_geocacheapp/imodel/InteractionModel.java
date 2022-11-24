package com.cmpt370_geocacheapp.imodel;

import android.location.Location;

import com.cmpt370_geocacheapp.model.PhysicalCacheObject;
import com.google.android.gms.maps.model.Polyline;

import java.util.ArrayList;
import java.util.function.Predicate;

public class InteractionModel {
    // Geocache attributes
    private PhysicalCacheObject currentlySelectedCache;
    private ArrayList<Predicate<PhysicalCacheObject>> currentFilters;
    private int maxDistance;
    // MVC attributes
    private final ArrayList<IModelListener> subscribers;
    private boolean selectedCachedChanged = false;
    private Location currentLocation;
    private Polyline currentCacheLine;

    /**
     * Constructor of the Interaction Model
     */
    public InteractionModel() {
        this.subscribers = new ArrayList<>();
        this.currentlySelectedCache = null;
        this.currentFilters = new ArrayList<>();
    }

    /**
     * Updates the currently selected filters
     *
     * @param newFilterList - The new filters
     */
    public void updateFilters(ArrayList<Predicate<PhysicalCacheObject>> newFilterList) {
        this.currentFilters = newFilterList;
        notifySubscribers();
    }

    /**
     * Adds a subscriber to the list of subscribers
     *
     * @param sub - a new subscriber
     */
    public void addSubscriber(IModelListener sub) {
        this.subscribers.add(sub);
    }

    /**
     * Notifies all subscribers that the Interaction Model has changed
     */
    private void notifySubscribers() {
        for (IModelListener sub : subscribers) {
            sub.iModelChanged();
        }
    }

    /**
     * Returns the currently selected cache
     *
     * @return - The selected cache
     */
    public PhysicalCacheObject getCurrentlySelectedCache() {
        return this.currentlySelectedCache;
    }

    /**
     * Sets the currently selected cache
     *
     * @param newlySelectedCache - The new cache to select
     */
    public void setCurrentlySelectedCache(PhysicalCacheObject newlySelectedCache) {
        this.currentlySelectedCache = newlySelectedCache;
        selectedCachedChanged = true;
        this.notifySubscribers();
        selectedCachedChanged = false;
    }

    /**
     * Clears the list of current filters
     */
    public void clearFilters() {
        this.currentFilters.clear();
    }


    /**
     * Gets the current list of filters
     *
     * @return - THe list of current filters
     */
    public ArrayList<Predicate<PhysicalCacheObject>> getCurrentFilters() {
        return this.currentFilters;
    }

    /**
     * Returns if the boolean flag for the selected cache being changed
     *
     * @return - True if the selected cache was changed, false otherwise
     */
    public boolean isSelectedCachedChanged() {
        return selectedCachedChanged;
    }

    /**
     * Sets the current location
     *
     * @param currentLocation - The new location
     */
    public void setCurrentLocation(Location currentLocation) {
        this.currentLocation = currentLocation;
        notifySubscribers();
    }

    /**
     * Sets the cache navigation line
     *
     * @param currentCacheLine - The cache navigation line
     */
    public void setCurrentCacheLine(Polyline currentCacheLine) {
        this.currentCacheLine = currentCacheLine;
    }

    /**
     * Returns the current location
     *
     * @return - The current location
     */
    public Location getCurrentLocation() {
        return currentLocation;
    }

    /**
     * Gets the navigation line
     *
     * @return - The navigation line
     */
    public Polyline getCurrentCacheLine() {
        return currentCacheLine;
    }

    /**
     * Sets the value for maximum distance (meters)
     *
     * @param distance - The max distance value
     */
    public void setMaxDistance(int distance) {
        this.maxDistance = distance;
    }

    /**
     * Returns the max distance value
     *
     * @return - Teh max distance value
     */
    public int getMaxDistance() {
        return maxDistance;
    }
}
