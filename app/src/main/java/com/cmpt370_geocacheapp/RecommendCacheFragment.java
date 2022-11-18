package com.cmpt370_geocacheapp;

import static android.content.Context.INPUT_METHOD_SERVICE;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.slider.RangeSlider;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class RecommendCacheFragment extends Fragment {

    //TODO: This is just a temporary fragment used for ensuring that the cache recommendation is working correctly

    ApplicationController controller;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Initialize view
        View view = inflater.inflate(R.layout.fragment_recommend_cache, container, false);

        // event handling stuff
        view.findViewById(R.id.cacheRecommendButton).setOnClickListener(this::getRecommendation);
        view.findViewById(R.id.clearRecommendationFiltersButton).setOnClickListener(this::clearFilters);

        return view;
    }

    private void clearFilters(View view) {
        ChipGroup cacheSizeChipGroup = requireView().findViewById(R.id.cacheSizeRecommendChipGroup);
        List<Integer> ids = cacheSizeChipGroup.getCheckedChipIds();
        for (Integer chipID : ids)
        {
            Chip chip = requireView().findViewById(chipID);
            chip.setChecked(false);
        }
        RangeSlider difficultySlider = requireView().findViewById(R.id.difficultyRecommendRangeSlider);
        difficultySlider.setValues(1f,5f);
        RangeSlider terrainDifficultySlider = requireView().findViewById(R.id.terrainRecommendRangeSlider);
        terrainDifficultySlider.setValues(1f,5f);
        RadioButton walkingRadioButton = requireView().findViewById(R.id.closeRadioButton);
        walkingRadioButton.setChecked(true);
    }

    private void getRecommendation(View view) {
        ArrayList<Predicate<PhysicalCacheObject>> filters = new ArrayList<>();

        // cache size filter
        ArrayList<Integer> selectedCacheSizes = new ArrayList<>();
        ChipGroup cacheSizeChipGroup = requireView().findViewById(R.id.cacheSizeRecommendChipGroup);
        List<Integer> ids = cacheSizeChipGroup.getCheckedChipIds();
        for (Integer id : ids) {
            if (id == R.id.microRecommendChip)
                selectedCacheSizes.add(1);
            if (id == R.id.smallRecommendChip)
                selectedCacheSizes.add(2);
            if (id == R.id.regularRecommendChip)
                selectedCacheSizes.add(3);
            if (id == R.id.largeRecommendChip)
                selectedCacheSizes.add(4);
            if (id == R.id.otherRecommendChip)
                selectedCacheSizes.add(5);
        }
        if (selectedCacheSizes.size() > 0) {
            Predicate<PhysicalCacheObject> cacheSizeFilter = cacheObject -> selectedCacheSizes.contains(cacheObject.getCacheSize());
            filters.add(cacheSizeFilter);
        }

        // cache difficulty filter
        RangeSlider difficultySlider = requireView().findViewById(R.id.difficultyRecommendRangeSlider);
        ArrayList<Float> difficultyRangeValues = (ArrayList<Float>) difficultySlider.getValues();
        if (difficultyRangeValues.get(0) != 1 || difficultyRangeValues.get(1) != 5) {
            Predicate<PhysicalCacheObject> cacheDifficultyFilter = cacheObject ->
                    cacheObject.getCacheDifficulty() >= difficultyRangeValues.get(0) && cacheObject.getCacheDifficulty() <= difficultyRangeValues.get(1);
            filters.add(cacheDifficultyFilter);
        }

        // cache terrain difficulty filter
        RangeSlider terrainDifficultySlider = requireView().findViewById(R.id.terrainRecommendRangeSlider);
        ArrayList<Float> terrainDifficultyRangeValues = (ArrayList<Float>) terrainDifficultySlider.getValues();
        if (terrainDifficultyRangeValues.get(0) != 1 || terrainDifficultyRangeValues.get(1) != 5) {
            Predicate<PhysicalCacheObject> terrainDifficultyFilter = cacheObject ->
                    cacheObject.getTerrainDifficulty() >= terrainDifficultyRangeValues.get(0) && cacheObject.getTerrainDifficulty() <= terrainDifficultyRangeValues.get(1);
            filters.add(terrainDifficultyFilter);
        }

        // cache distance
        RadioGroup rg = requireView().findViewById(R.id.cacheDistanceRadioGroup);
        int selectedID = rg.getCheckedRadioButtonId();
        int distance = -1;
        if (selectedID == R.id.closeRadioButton)
            distance = 1500;
        if (selectedID == R.id.averageRadioButton)
            distance = 3000;
        if (selectedID == R.id.farRadioButton)
            distance = 7500;

        // TODO: Add a time selector to give finer distance control. Eg: 10 minutes, 20 minutes, 30 minutes

        // get recommended cache
        boolean success = controller.getRecommendedCache(filters, distance);

        if (success) {
            //hide filter fragment on apply
            FragmentManager fm = getParentFragmentManager();
            fm.beginTransaction().hide(this).commit();
        } else {
            Toast.makeText(this.getContext(), "No caches matching criteria, please adjust filters and try again.", Toast.LENGTH_LONG).show();
        }
    }


    public void setController(ApplicationController newController) {
        this.controller = newController;
    }


}