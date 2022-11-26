package com.cmpt370_geocacheapp.views;

import static android.content.Context.INPUT_METHOD_SERVICE;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.cmpt370_geocacheapp.R;
import com.cmpt370_geocacheapp.controller.ApplicationController;
import com.cmpt370_geocacheapp.model.ApplicationModel;

public class SearchCacheFragment extends Fragment {

    //TODO: This is just a temporary fragment used for ensuring that the cache search code could be applied correctly

    ApplicationController controller;
    ApplicationModel model;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Initialize view
        View view = inflater.inflate(R.layout.fragment_search_cache, container, false);

        // event handling stuff
        view.findViewById(R.id.cacheSearchButton).setOnClickListener(this::search);
        view.findViewById(R.id.searchCloseButton).setOnClickListener(this::close);

        return view;
    }

    /**
     * Performs a search using the selected search method and search input
     */
    private void search(View view) {
        EditText searchEditText = requireView().findViewById(R.id.searchEditText);
        String searchInput = searchEditText.getText().toString();
        if (searchInput.equals("")) {
            Toast.makeText(this.getContext(), "Please enter a search term", Toast.LENGTH_SHORT).show();
            return;
        }

        RadioGroup searchTypeRadioGroup = requireView().findViewById(R.id.searchTypeRadioGroup);
        int selectedId = searchTypeRadioGroup.getCheckedRadioButtonId();
        int searchType = -1;
        if (selectedId == R.id.searchNameRadioButton)
            searchType = 1;
        else if (selectedId == R.id.searchAuthorRadioButton)
            searchType = 2;
        else if (selectedId == R.id.searchIDRadioButton)
            searchType = 3;

        if (searchType == 1) {
            if (!controller.searchByName(searchInput)) {
                Toast.makeText(this.getContext(), "No caches found", Toast.LENGTH_SHORT).show();
                return;
            }
        } else if (searchType == 2) {
            if (!controller.searchByAuthorName(searchInput)) {
                Toast.makeText(this.getContext(), "No caches found", Toast.LENGTH_SHORT).show();
                return;
            }
        } else if (searchType == 3) {
            long searchID;
            try {
                searchID = Long.parseLong(searchInput);
            } catch (NumberFormatException e) {
                Toast.makeText(this.getContext(), "Please enter a valid cacheID so search by", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!controller.searchByCacheID(searchID)) {
                Toast.makeText(this.getContext(), "No Cache matches search ID", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        close(view);
    }

    /**
     * Closes the search view
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

    public void setController(ApplicationController newController) {
        this.controller = newController;
    }

    public void setModel(ApplicationModel newModel) {
        this.model = newModel;
    }

}