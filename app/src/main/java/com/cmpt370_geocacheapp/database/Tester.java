package com.cmpt370_geocacheapp.database;

import android.app.Application;
import android.content.Context;

public class Tester extends Application {
    private Context context;

    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        doit();
    }

    public void initDummyData(DatabaseController dbController) {
        dbController.db.execSQL("DROP TABLE IF EXISTS " + GeocacheDef.TABLE_NAME);
        dbController.db.execSQL(GeocacheDef.SQL_CREATE_ENTRIES);
        dbController.addGeocache("haha lol", (float)1.2, (float)3.4);
        dbController.addGeocache("nein", (float)9, (float)9);
        dbController.addGeocache("ayt", (float)8.5, (float)8.5);
    }

    public void doit() {
        DatabaseController dbController = new DatabaseController(context);
        dbController.open();
        initDummyData(dbController);
        dbController.fetchByDistance(3, 10, 10);
        dbController.close();
    }
}
