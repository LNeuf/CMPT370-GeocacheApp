package com.cmpt370_geocacheapp;

public class MainUI {
    /**
     * MainUI for all views and setting up MVC structure
     */
    public MainUI() {
        // create all MVC components - view, controller, model, and iModel
        SampleView testView = new SampleView();
        ApplicationController controller = new ApplicationController();
        ApplicationModel model = new ApplicationModel();
        InteractionModel iModel = new InteractionModel();

        // link all components
        testView.setController(controller);
        controller.setModel(model);
        controller.setInteractionModel(iModel);
        testView.setModel(model);
        testView.setIModel(iModel);

        // make view receive model/iModel updates
        model.addSubscriber(testView);
        iModel.addSubscriber(testView);

    }

}
