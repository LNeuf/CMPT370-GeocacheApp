package com.cmpt370_geocacheapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class CacheCreateFragment extends Fragment{

    ApplicationController controller;
    ApplicationModel model;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Initialize view
        View view = inflater.inflate(R.layout.fragment_cache_create, container, false);

        return view;
    }

    public void setController(ApplicationController newController) {
        this.controller = newController;
    }

    public void setModel(ApplicationModel newModel)
    {
        this.model = newModel;
    }
}