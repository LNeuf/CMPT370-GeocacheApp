package com.cmpt370_geocacheapp;

public class SampleView implements ModelListener, IModelListener {
    // attributes
    ApplicationModel model;
    InteractionModel iModel;

    /**
     * Constructor for this Sample View
     */
    public SampleView() {
        // this is a sample view class for structure setup only
        // unsure how Android studio view actually interact
        // This is just a placeholder class
    }

    /**
     * Sets the SampleView's model
     *
     * @param model - The model
     */
    public void setModel(ApplicationModel model) {
        this.model = model;
    }

    /**
     * Sets the SampleView's iModel
     *
     * @param iModel - The iModel
     */
    public void setIModel(InteractionModel iModel) {
        this.iModel = iModel;
    }

    /**
     * Sets the controller events needed for the SampleView
     *
     * @param controller - The controller to handle all events with the backend
     */
    public void setController(ApplicationController controller) {
        // Where event handling or event listeners are placed
        // This is a placeholder for attaching events to the various view components
    }

    /**
     * IModel changed, update view components
     */
    @Override
    public void iModelChanged() {
        // update view components that interact or rely on the Interaction model
        // selected cache
    }

    /**
     * Model changed, update view components
     */
    @Override
    public void modelChanged() {
        // update view components that interact or rely on the backend model
        // filtered/searched cache list
    }
}
