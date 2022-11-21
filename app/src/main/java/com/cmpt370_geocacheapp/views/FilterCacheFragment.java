package com.cmpt370_geocacheapp.views;

import static android.content.Context.INPUT_METHOD_SERVICE;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.cmpt370_geocacheapp.controller.ApplicationController;
import com.cmpt370_geocacheapp.model.ApplicationModel;
import com.cmpt370_geocacheapp.imodel.InteractionModel;
import com.cmpt370_geocacheapp.model.PhysicalCacheObject;
import com.cmpt370_geocacheapp.R;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.slider.RangeSlider;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class FilterCacheFragment extends Fragment {

    //TODO: This is just a temporary fragment used for ensuring that the filter-applying code could be applied correctly

    ApplicationController controller;
    ApplicationModel model;
    InteractionModel iModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Initialize view
        View view = inflater.inflate(R.layout.fragment_filter_cache, container, false);

        // event handling stuff
        view.findViewById(R.id.cacheFilterButton).setOnClickListener(this::filterCaches);
        view.findViewById(R.id.clearFilterButton).setOnClickListener(this::clearFilters);
        view.findViewById(R.id.filterCloseButton).setOnClickListener(this::close);

        return view;
    }

    private void close(View view) {
        //hide filter fragment on apply
        FragmentManager fm = getParentFragmentManager();
        fm.beginTransaction().hide(this).commit();
    }

    private void clearFilters(View view) {
        ChipGroup cacheSizeChipGroup = requireView().findViewById(R.id.cacheSizeChipGroup);
        List<Integer> ids = cacheSizeChipGroup.getCheckedChipIds();
        for (Integer chipID : ids)
        {
            Chip chip = requireView().findViewById(chipID);
            chip.setChecked(false);
        }
        RangeSlider difficultySlider = requireView().findViewById(R.id.difficultyRangeSlider);
        difficultySlider.setValues(1f,5f);
        RangeSlider terrainDifficultySlider = requireView().findViewById(R.id.terrainRangeSlider);
        terrainDifficultySlider.setValues(1f,5f);
        EditText distanceInput = requireView().findViewById(R.id.distanceEditText);
        distanceInput.setText("");
        controller.setMaxDistance(-1);
        controller.applyFilters(new ArrayList<>());
        // hide keyboard on button click
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

    private void filterCaches(View view) {
        ArrayList<Predicate<PhysicalCacheObject>> filters = new ArrayList<>();

        // cache size filter
        ArrayList<Integer> selectedCacheSizes = new ArrayList<>();
        ChipGroup cacheSizeChipGroup = requireView().findViewById(R.id.cacheSizeChipGroup);
        List<Integer> ids = cacheSizeChipGroup.getCheckedChipIds();
        for (Integer id : ids) {
            if (id == R.id.microChip)
                selectedCacheSizes.add(1);
            if (id == R.id.smallChip)
                selectedCacheSizes.add(2);
            if (id == R.id.regularChip)
                selectedCacheSizes.add(3);
            if (id == R.id.largeChip)
                selectedCacheSizes.add(4);
            if (id == R.id.otherChip)
                selectedCacheSizes.add(5);
        }
        if (selectedCacheSizes.size() > 0) {
            Predicate<PhysicalCacheObject> cacheSizeFilter = cacheObject -> selectedCacheSizes.contains(cacheObject.getCacheSize());
            filters.add(cacheSizeFilter);
        }

        // cache difficulty filter
        RangeSlider difficultySlider = requireView().findViewById(R.id.difficultyRangeSlider);
        ArrayList<Float> difficultyRangeValues = (ArrayList<Float>) difficultySlider.getValues();
        if (difficultyRangeValues.get(0) != 1 || difficultyRangeValues.get(1) != 5) {
            Predicate<PhysicalCacheObject> cacheDifficultyFilter = cacheObject ->
                    cacheObject.getCacheDifficulty() >= difficultyRangeValues.get(0) && cacheObject.getCacheDifficulty() <= difficultyRangeValues.get(1);
            filters.add(cacheDifficultyFilter);
        }

        // cache terrain difficulty filter
        RangeSlider terrainDifficultySlider = requireView().findViewById(R.id.terrainRangeSlider);
        ArrayList<Float> terrainDifficultyRangeValues = (ArrayList<Float>) terrainDifficultySlider.getValues();
        if (terrainDifficultyRangeValues.get(0) != 1 || terrainDifficultyRangeValues.get(1) != 5) {
            Predicate<PhysicalCacheObject> terrainDifficultyFilter = cacheObject ->
                    cacheObject.getTerrainDifficulty() >= terrainDifficultyRangeValues.get(0) && cacheObject.getTerrainDifficulty() <= terrainDifficultyRangeValues.get(1);
            filters.add(terrainDifficultyFilter);
        }


        EditText distanceInput = requireView().findViewById(R.id.distanceEditText);
        int distance = -1;
        if (!distanceInput.getText().toString().equals("")) {
            try {
                distance = Integer.parseInt(distanceInput.getText().toString());
            } catch (NumberFormatException e) {
                Toast.makeText(this.getContext(), "Improper distance value entered", Toast.LENGTH_SHORT).show();
                return;
            }
        }


        // apply the filters
        controller.setSelectedCache(null);
        controller.setMaxDistance(distance);
        controller.applyFilters(filters);
        // hide keyboard on button click
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


    public void setController(ApplicationController newController) {
        this.controller = newController;
    }

    public void setModel(ApplicationModel newModel) {
        this.model = newModel;
    }

    public void setIModel(InteractionModel newIModel) {
        this.iModel = newIModel;
    }

}