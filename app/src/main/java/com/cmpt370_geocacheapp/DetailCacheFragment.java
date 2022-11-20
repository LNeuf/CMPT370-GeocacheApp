package com.cmpt370_geocacheapp;

import static android.content.Context.INPUT_METHOD_SERVICE;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.SphericalUtil;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class DetailCacheFragment extends Fragment implements ModelListener {

    //TODO: This is just a temporary fragment for showing cache details
    ListView lv;
    private CommentListViewAdapter listViewAdapter;
    private ArrayList<CommentListItem> items = new ArrayList<>();
    ApplicationController controller;
    ApplicationModel model;
    InteractionModel iModel;
    long currentGeocacheID;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Initialize view
        View view = inflater.inflate(R.layout.fragment_cache_detail, container, false);

        // event handling stuff
        view.findViewById(R.id.detailCreateCommentbutton).setOnClickListener(this::comment);
        view.findViewById(R.id.detailCloseButton).setOnClickListener(this::close);

        return view;
    }

    private void comment(View view) {
        EditText contentsEditText = requireView().findViewById(R.id.detailCommentEditTextTextMultiLine);
        String contents = contentsEditText.getText().toString();
        if (contents.equals(""))
        {
            Toast.makeText(this.getContext(), "Please enter a comment", Toast.LENGTH_SHORT).show();
            return;
        }

        // TODO: actually include proper user
        String username = "Jesse";

        RadioGroup ratingRadioGroup = requireView().findViewById(R.id.ratingsRadioGroup);
        int selectedId = ratingRadioGroup.getCheckedRadioButtonId();
        int rating = -1;
        if (selectedId == R.id.ratingsOneRadioButton)
            rating = 1;
        else if (selectedId == R.id.ratingsTwoRadioButton)
            rating = 2;
        else if (selectedId == R.id.ratingsThreeRadioButton)
            rating = 3;
        else if (selectedId == R.id.ratingsFourRadioButton)
            rating = 4;
        else if (selectedId == R.id.ratingsFiveRadioButton)
            rating = 5;

        // actually create a new rating/comment
        controller.createRating(username, contents, rating, currentGeocacheID);

        close(view);
    }

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

    public void setIModel(InteractionModel newIModel) {
        this.iModel = newIModel;
    }

    @Override
    public void modelChanged() {

    }

    public void setCacheID(String snippet) {
        // set cache detail information
        long cacheID = Long.parseLong(snippet);
        PhysicalCacheObject selectedCache = model.getCacheById(cacheID);
        TextView textToEdit = requireView().findViewById(R.id.detailCacheNametextView);
        textToEdit.setText(selectedCache.getCacheName());

        textToEdit = requireView().findViewById(R.id.detailDistanceEdit);
        String distanceString = "N/A";
        if (iModel.getCurrentLocation() != null) {
            int distance = (int) SphericalUtil.computeDistanceBetween(new LatLng(
                            iModel.getCurrentLocation().getLatitude(),
                            iModel.getCurrentLocation().getLongitude()),
                    new LatLng(selectedCache.getCacheLatitude(),
                            selectedCache.getCacheLongitude()));
            distanceString = String.valueOf(distance) + " m";
        }
        textToEdit.setText(distanceString);

        textToEdit = requireView().findViewById(R.id.detailAuthorEdit);
        textToEdit.setText(selectedCache.getCacheAuthor());

        textToEdit = requireView().findViewById(R.id.detailDiffEdit);
        textToEdit.setText(String.format("%d / 5", selectedCache.getCacheDifficulty()));

        textToEdit = requireView().findViewById(R.id.detailTerrEdit);
        textToEdit.setText(String.format("%d / 5", selectedCache.getTerrainDifficulty()));

        textToEdit = requireView().findViewById(R.id.detailSizeEdit);
        textToEdit.setText(selectedCache.getCacheSizeString());

        // get all comments for a cache
        this.items = (ArrayList<CommentListItem>) this.model.getCacheRatings(cacheID).stream()
                .map(rating -> new CommentListItem(rating.userUsername, rating.contents,
                        rating.rating, rating.geocacheId)).collect(Collectors.toList());

        // set cache comments
        lv = requireView().findViewById(R.id.detailCommentListView);
        listViewAdapter = new CommentListViewAdapter(this.getContext(),items);
        lv.setAdapter(listViewAdapter);

        this.currentGeocacheID = cacheID;


    }
}