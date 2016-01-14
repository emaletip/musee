package com.example.xo7.lemusedelafaunedasie;

import android.app.Application;

import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;

/**
 * Created by Mowglie on 09/12/2015.
 */
public class MyApp extends Application {

    private static MyApp instance;
    private static Bus bus;

    private AnimalDao datasource;

    public void initAnimalsTable()  {
        datasource.emptyTable();
        datasource.createAnimal("test", "description", "herbivore", "animal_1_1", "45.1", "-45.1", "prop1:a@prop2:z");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        bus = new Bus(ThreadEnforcer.MAIN);

        datasource = new AnimalDao(this);
        datasource.open();

        initAnimalsTable();

    }

    public static MyApp getInstance() {
        return instance;
    }

    public static Bus getBus() {
        return bus;
    }
}
