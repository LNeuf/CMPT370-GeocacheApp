package com.cmpt370_geocacheapp;

import java.util.ArrayList;

public class ApplicationModel {
    // MVC attributes
    private ArrayList<ModelListener> subscribers;
    // Geocache attributes
    private ArrayList<GeoCache> nearbyCacheList;
    private ArrayList<GeoCache> filteredCacheList;

    /**
     * Constructor of the applications model
     */
    public ApplicationModel() {
        this.nearbyCacheList = new ArrayList<>();
        this.filteredCacheList = new ArrayList<>();
        this.subscribers = new ArrayList<>();
    }

    /**
     * Adds a new subscriber to the list of subscribers
     *
     * @param sub - The new subscriber
     */
    public void addSubscriber(ModelListener sub) {
        this.subscribers.add(sub);
    }

    /**
     * Notifies all subscribers that the model has changed
     */
    private void notifySubscribers() {
        for (int i = 0; i < subscribers.size(); i++) {
            subscribers.get(i).modelChanged();
        }
        //subscribers.forEach(ModelListener::modelChanged); // Need API version 24, current minimum is version 23
    }


}
