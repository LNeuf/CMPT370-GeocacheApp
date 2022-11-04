package com.cmpt370_geocacheapp;

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

    public void setSelectedCache(GeoCache selectedCache) {
        iModel.setCurrentlySelectedCache(selectedCache);
    }

    public void createCache(String cacheName, int cacheSize, int cacheDifficulty, int i, String cacheCreator, float latitude, float longitude) {
        GeoCache newCache = new GeoCache(cacheName,cacheSize,cacheDifficulty,i,cacheCreator,latitude,longitude);
        model.addNewCache(newCache);
        model.updateFilteredCacheList(iModel.getCurrentFilters());
        iModel.setCurrentlySelectedCache(newCache);
    }
}
