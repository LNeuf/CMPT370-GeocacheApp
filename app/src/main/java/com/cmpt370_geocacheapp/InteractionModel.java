package com.cmpt370_geocacheapp;

import java.util.ArrayList;

public class InteractionModel {
    // Geocache attributes
    private GeoCache currentlySelectedCache;
    private float currentLongitude;
    private float currentLatitude;
    // MVC attributes
    private ArrayList<IModelListener> subscribers;

    /**
     * Constructor of the Interaction Model
     */
    public InteractionModel() {
        this.subscribers = new ArrayList<>();
        this.currentlySelectedCache = null;
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
        for (int i = 0; i < subscribers.size(); i++) {
            subscribers.get(0).iModelChanged();
        }
        //subscribers.forEach(IModelListener::iModelChanged); // API version 24 needed, currently set to minimum 23
    }

    public GeoCache getCurrentlySelectedCache() {
        return this.currentlySelectedCache;
    }

    public void setCurrentlySelectedCache(GeoCache newlySelectedCache) {
        this.currentlySelectedCache = newlySelectedCache;
        this.notifySubscribers();
    }


}
