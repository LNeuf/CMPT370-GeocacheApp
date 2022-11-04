package com.cmpt370_geocacheapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.cmpt370_geocacheapp.databinding.ActivityHomeBinding;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

public class MainActivity extends AppCompatActivity implements IModelListener{

    ActivityHomeBinding binding;
    MapFragment mapFragment;
    ListFragment listFragment;
    CacheCreateFragment cacheCreateFragment;
    ApplicationController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // setup MVC stuff
        ApplicationModel model = new ApplicationModel();
        InteractionModel iModel = new InteractionModel();
        controller = new ApplicationController();

        controller.setModel(model);
        controller.setInteractionModel(iModel);

        // Create all fragments
        mapFragment = new MapFragment();
        listFragment = new ListFragment();
        cacheCreateFragment = new CacheCreateFragment();

        // MVC linking
        mapFragment.setModel(model);
        listFragment.setModel(model);
        cacheCreateFragment.setModel(model);

        mapFragment.setIModel(iModel);
        listFragment.setIModel(iModel);


        mapFragment.setController(controller);
        listFragment.setController(controller);
        cacheCreateFragment.setController(controller);

        model.addSubscriber(mapFragment);
        model.addSubscriber(listFragment);

        iModel.addSubscriber(mapFragment);
        iModel.addSubscriber(listFragment);
        iModel.addSubscriber(this);

        // initialize model
        model.init();

        // add all fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.frame_layout, mapFragment);
        fragmentTransaction.add(R.id.frame_layout, listFragment);
        fragmentTransaction.add(R.id.frame_layout, cacheCreateFragment);
        fragmentTransaction.commit();

        // show map fragment by default on startup
        showFragment(mapFragment);
        hideFragment(listFragment);
        hideFragment(cacheCreateFragment);

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId())
            {
                case R.id.nav_map:
                    showFragment(mapFragment);
                    hideFragment(listFragment);
                    hideFragment(cacheCreateFragment);
                    break;
                case R.id.nav_list:
                    showFragment(listFragment);
                    hideFragment(mapFragment);
                    hideFragment(cacheCreateFragment);
                    break;
                case R.id.nav_create:
                    showFragment(cacheCreateFragment);
                    hideFragment(mapFragment);
                    hideFragment(listFragment);
                    break;
            }

            return true;
        });
    }


    private void showFragment(Fragment fragment)
    {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.show(fragment);
        fragmentTransaction.commit();
    }

    private void hideFragment(Fragment fragment)
    {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.hide(fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void iModelChanged() {
        // if the selected cache cached has changed, hide all fragments except mapfragment
        hideFragment(listFragment);
        hideFragment(cacheCreateFragment);
        showFragment(mapFragment);
        binding.bottomNavigationView.setSelectedItemId(R.id.nav_map);
    }

    public void createCache(View view) {

        EditText textInput = findViewById(R.id.cacheNameEditText);
        String cacheName = textInput.getText().toString();
        if (cacheName.equals("")) {
            Toast.makeText(this, "Please enter a cache name", Toast.LENGTH_SHORT).show();
            return;
        }

        textInput = findViewById(R.id.latitudeEditText);
        float latitude;
        try {
            latitude = Float.parseFloat(textInput.getText().toString());
        } catch (NumberFormatException e)
        {
            Toast.makeText(this, "Improper latitude entered", Toast.LENGTH_SHORT).show();
            return;
        }
        if (latitude < -90 || latitude > 90)
            Toast.makeText(this, "Improper latitude entered", Toast.LENGTH_SHORT).show();

        textInput = findViewById(R.id.longitudeEditText);
        float longitude;
        try {
            longitude = Float.parseFloat(textInput.getText().toString());
        } catch (NumberFormatException e)
        {
            Toast.makeText(this, "Improper longitude entered", Toast.LENGTH_SHORT).show();
            return;
        }
        if (longitude < -180 || longitude > 180)
            Toast.makeText(this, "Improper longitude entered", Toast.LENGTH_SHORT).show();

        // TODO fix cache size names
        int cacheSize = -1;
        RadioGroup group = findViewById(R.id.cacheSizeRadioGroup);

        int selectedId = group.getCheckedRadioButtonId();
        if (selectedId == R.id.cacheRadioSizeSmall)
            cacheSize = 1;
        else if (selectedId == R.id.cacheRadioSizeMed)
            cacheSize = 2;
        else if (selectedId == R.id.cacheRadioSizeLarge)
            cacheSize = 3;

        if (cacheSize == -1)
        {
            Toast.makeText(this, "Invalid cache size", Toast.LENGTH_SHORT).show();
            return;
        }

        // TODO make sure difficulty is correct for real geocache object
        SeekBar cacheDifficultySeekBar = findViewById(R.id.cacheDifficultySeekBar);
        int cacheDifficulty = cacheDifficultySeekBar.getProgress();

        // TODO: Assign creator name based on who is logged in
        String cacheCreator = "Test Creator";

        controller.createCache(cacheName, cacheSize, cacheDifficulty, 1, cacheCreator, latitude, longitude);
    }
}