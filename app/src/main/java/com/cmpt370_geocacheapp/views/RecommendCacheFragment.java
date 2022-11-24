package com.cmpt370_geocacheapp.views;

import static android.content.Context.INPUT_METHOD_SERVICE;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.cmpt370_geocacheapp.controller.ApplicationController;
import com.cmpt370_geocacheapp.model.PhysicalCacheObject;
import com.cmpt370_geocacheapp.R;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.slider.RangeSlider;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class RecommendCacheFragment extends Fragment {

    ApplicationController controller;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Initialize view
        View view = inflater.inflate(R.layout.fragment_recommend_cache, container, false);

        // event handling stuff
        view.findViewById(R.id.cacheRecommendButton).setOnClickListener(this::getRecommendation);
        view.findViewById(R.id.clearRecommendationFiltersButton).setOnClickListener(this::clearFilters);
        view.findViewById(R.id.recommendCloseButton).setOnClickListener(this::close);

        return view;
    }

    /**
     * Close the recommended cache window
     */
    private void close(View view) {
        // hide keyboard on close
        try {
            this.getContext();
            InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(requireActivity().getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
            // keyboard wasn't up
        }
        //hide filter fragment on apply
        FragmentManager fm = getParentFragmentManager();
        fm.beginTransaction().hide(this).commit();
    }

    /**
     * Clears the current filter settings in this view
     */
    private void clearFilters(View view) {
        ChipGroup cacheSizeChipGroup = requireView().findViewById(R.id.cacheSizeRecommendChipGroup);
        List<Integer> ids = cacheSizeChipGroup.getCheckedChipIds();
        for (Integer chipID : ids) {
            Chip chip = requireView().findViewById(chipID);
            chip.setChecked(false);
        }
        RangeSlider difficultySlider = requireView().findViewById(R.id.difficultyRecommendRangeSlider);
        difficultySlider.setValues(1f, 5f);
        RangeSlider terrainDifficultySlider = requireView().findViewById(R.id.terrainRecommendRangeSlider);
        terrainDifficultySlider.setValues(1f, 5f);
        RadioButton walkingRadioButton = requireView().findViewById(R.id.closeRadioButton);
        walkingRadioButton.setChecked(true);
    }

    /**
     * Gets a cache recommendation using the current filter criteria
     */
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

        // cache travel method
        RadioGroup rg = requireView().findViewById(R.id.cacheDistanceRadioGroup);
        int selectedID = rg.getCheckedRadioButtonId();
        double speed = -1;
        if (selectedID == R.id.closeRadioButton)
            speed = 2.5;
        if (selectedID == R.id.averageRadioButton)
            speed = 10;
        if (selectedID == R.id.farRadioButton)
            speed = 50;

        // time to cache
        rg = requireView().findViewById(R.id.cacheTimeRadioGroup);
        selectedID = rg.getCheckedRadioButtonId();
        double time = -1;
        if (selectedID == R.id.tenMinuteRadioButton)
            time = 1.0 / 6;
        if (selectedID == R.id.twentyMinuteRadioButton)
            time = 2.0 / 6;
        if (selectedID == R.id.thirtyMinuteRadioButton)
            time = 3.0 / 6;


        // get recommended cache
        boolean success = controller.getRecommendedCache(filters, (int) ((speed * time) * 1000));

        if (success) {
            close(view);
        } else {
            Toast.makeText(this.getContext(), "No caches matching criteria, please adjust filters and try again.", Toast.LENGTH_LONG).show();
        }
    }

    public void setController(ApplicationController newController) {
        this.controller = newController;
    }

}