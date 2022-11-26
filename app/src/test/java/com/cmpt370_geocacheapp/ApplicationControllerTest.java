package com.cmpt370_geocacheapp;

import com.cmpt370_geocacheapp.controller.ApplicationController;
import com.cmpt370_geocacheapp.imodel.InteractionModel;
import com.cmpt370_geocacheapp.model.ApplicationModel;
import com.cmpt370_geocacheapp.model.CacheObject;
import com.cmpt370_geocacheapp.model.PhysicalCacheObject;
import com.cmpt370_geocacheapp.model.User;

import junit.framework.TestCase;

public class ApplicationControllerTest extends TestCase {


    public void testSetSelectedCache() {
        ApplicationController testController = new ApplicationController();
        InteractionModel testIModel = new InteractionModel();
        testController.setInteractionModel(testIModel);
        PhysicalCacheObject testCache = new PhysicalCacheObject(new CacheObject("TestName", new User("TestAuthor","TEstPass",123), 123),50,80,1,2,3 );
        testController.setSelectedCache(testCache);
        assertEquals(testCache, testIModel.getCurrentlySelectedCache());
    }

    public void testCreateCache() {
        ApplicationController testController = new ApplicationController();
        ApplicationModel testModel = new ApplicationModel();
        testController.setModel(testModel);
        InteractionModel testIModel = new InteractionModel();
        testController.setInteractionModel(testIModel);
        testController.createCache("TestName", new User("TestAuthor","TEstPass",123), 50,80,1,2,3,null);
        assertEquals(testIModel.getCurrentlySelectedCache(), testModel.getFilteredCacheList().get(29)); // Controller makes model create cache, then sets IModel to select that cache
    }
}