package com.cmpt370_geocacheapp;

import java.util.ArrayList;
import java.util.function.Predicate;

public class InteractionModel {
    // Geocache attributes
    private GeoCache currentlySelectedCache;
    private float currentLongitude;
    private float currentLatitude;
    private ArrayList<Predicate<GeoCache>> currentFilters;
    // MVC attributes
    private ArrayList<IModelListener> subscribers;

    /**
     * Constructor of the Interaction Model
     */
    public InteractionModel() {
        this.subscribers = new ArrayList<>();
        this.currentlySelectedCache = null;
        this.currentFilters = new ArrayList<Predicate<GeoCache>>();
    }

    /**
     * Adds a subscriber to the list of subscribers
     * @param sub - a new subscriber
     */
    public void addSubscriber(IModelListener sub) {
        this.subscribers.add(sub);
    }

    /**
     * Notifies all subscribers that the Interaction Model has changed
     */
    private void notifySubscribers() {
        for (IModelListener sub : subscribers)
        {
            sub.iModelChanged();
        }
    }

    public GeoCache getCurrentlySelectedCache() {
        return this.currentlySelectedCache;
    }

    public void setCurrentlySelectedCache(GeoCache newlySelectedCache) {
        this.currentlySelectedCache = newlySelectedCache;
        this.notifySubscribers();
    }

    public void clearFilters()
    {
        this.currentFilters.clear();
    }


    public ArrayList<Predicate<GeoCache>> getCurrentFilters() {
        return new ArrayList<>(); // no filters yet
    }
}
