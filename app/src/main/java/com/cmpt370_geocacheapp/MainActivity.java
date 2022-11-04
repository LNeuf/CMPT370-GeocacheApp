package com.cmpt370_geocacheapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.cmpt370_geocacheapp.databinding.ActivityHomeBinding;

public class MainActivity extends AppCompatActivity {

    ActivityHomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Create all fragments
        MapFragment mapFragment = new MapFragment();
        ListFragment listFragment = new ListFragment();
        CacheCreateFragment cacheCreateFragment = new CacheCreateFragment();

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
}