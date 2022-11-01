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

        // create model facade
        ModelFacade facade = new ModelFacade();


        // link all components
        testView.setController(controller);
        controller.setModel(model);
        controller.setInteractionModel(iModel);
        testView.setFacade(facade);
        testView.setIModel(iModel);
        // link model and facade
        facade.setModel(model);
        model.setFacade(facade);

        // make view receive model/iModel updates
        facade.addSubscriber(testView); // view receives model updates through the facade
        iModel.addSubscriber(testView);

    }

}
