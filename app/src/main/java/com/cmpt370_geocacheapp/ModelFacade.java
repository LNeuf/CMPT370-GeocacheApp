package com.cmpt370_geocacheapp;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ModelFacade {

    // attributes - model and list of classes to be notified on model updates
    ApplicationModel model;
    ArrayList<ModelListener> modelSubscribers;

    /**
     * Facade for creating a simplified API for getting cache information
     */
    public ModelFacade()
    {
        // empty
    }

    /**
     * Sets the model
     * @param newModel - the model
     */
    public void setModel(ApplicationModel newModel)
    {
        this.model = newModel;
    }

    /**
     * Adds a new subscriber to the list of subscribers
     *
     * @param sub - The new subscriber
     */
    public void addSubscriber(ModelListener sub) {
        this.modelSubscribers.add(sub);
    }

    /**
     * Notifies all subscribers that the model has changed
     */
    public void notifyModelSubscribers() {
        for (int i = 0; i < modelSubscribers.size(); i++) {
            modelSubscribers.get(i).modelChanged();
        }
    }

    /**
     * Method to update the filtered cache list based on a list of predicates
     * @param filterList - The list of filters/predicates to filter the cache list by
     */
    public void updateFilteredCacheList(ArrayList<Predicate<GeoCache>> filterList)
    {
        this.model.updateFilteredCacheList(filterList);
    }

}
