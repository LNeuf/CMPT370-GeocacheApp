package com.cmpt370_geocacheapp.views;

import static android.content.Context.INPUT_METHOD_SERVICE;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.cmpt370_geocacheapp.controller.ApplicationController;
import com.cmpt370_geocacheapp.model.ApplicationModel;
import com.cmpt370_geocacheapp.imodel.InteractionModel;
import com.cmpt370_geocacheapp.model.ModelListener;
import com.cmpt370_geocacheapp.model.PhysicalCacheObject;
import com.cmpt370_geocacheapp.R;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.SphericalUtil;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class DetailCacheFragment extends Fragment {

    ListView lv;
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
        view.findViewById(R.id.detailCreateCommentbutton).setOnClickListener(this::createComment);
        view.findViewById(R.id.detailCloseButton).setOnClickListener(this::closeWindow);
        view.findViewById(R.id.cacheDeleteButton).setOnClickListener(this::deleteCache);

        return view;
    }

    /**
     * Deletes the current cache
     */
    private void deleteCache(View view) {
        controller.deleteCache(this.currentGeocacheID);
        closeWindow(view);
    }

    /**
     * Creates a new comment from rating selected and text input
     */
    private void createComment(View view) {
        EditText contentsEditText = requireView().findViewById(R.id.detailCommentEditTextTextMultiLine);
        String contents = contentsEditText.getText().toString();
        if (contents.equals("")) {
            Toast.makeText(this.getContext(), "Please enter a comment", Toast.LENGTH_SHORT).show();
            return;
        }

        // TODO: actually include proper user based on who is logged in
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

        closeWindow(view);
    }

    /**
     * CLoses the detail window for this cache
     */
    private void closeWindow(View view) {
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

    /**
     * Sets the detail fragments information to show correct information from the proper cache
     *
     * @param snippet - The snippet from the map marker which contains the cache ID
     */
    public void setCacheInfo(String snippet) {

        boolean alreadyRated = false;
        // set cache detail information
        long cacheID = Long.parseLong(snippet);
        PhysicalCacheObject selectedCache = model.getCacheById(cacheID);
        TextView textToEdit = requireView().findViewById(R.id.detailCacheNametextView);
        textToEdit.setText(selectedCache.getCacheName());

        // get all comments for a cache
        ArrayList<CommentListItem> items = (ArrayList<CommentListItem>) this.model.getCacheRatings(cacheID).stream()
                .map(rating -> new CommentListItem(rating.userUsername, rating.contents,
                        rating.rating, rating.geocacheId)).collect(Collectors.toList());

        textToEdit = requireView().findViewById(R.id.detailDistanceEdit);
        String distanceString = "N/A";
        if (iModel.getCurrentLocation() != null) {
            int distance = (int) SphericalUtil.computeDistanceBetween(new LatLng(
                            iModel.getCurrentLocation().getLatitude(),
                            iModel.getCurrentLocation().getLongitude()),
                    new LatLng(selectedCache.getCacheLatitude(),
                            selectedCache.getCacheLongitude()));
            distanceString = distance + " m";
        }
        textToEdit.setText(distanceString);

        // get the caches rating, make sure it cant be rated twice by the same user
        textToEdit = requireView().findViewById(R.id.detailRatingScoreEdit);
        String ratingsScoreString;
        double cacheRating = selectedCache.getCacheRating();
        for (CommentListItem rating : items) {
            if (rating.getAuthor().equals("Jesse")) // TODO: need to user signed in user's name
            {
                alreadyRated = true;
                break;
            }
        }
        ratingsScoreString = (items.size() > 0 ? String.format("%.1f / 5.0 - Out of %d ratings", cacheRating, items.size()) : "0.0 / 5.0 - Not yet rated");
        textToEdit.setText(ratingsScoreString);

        textToEdit = requireView().findViewById(R.id.detailAuthorEdit);
        textToEdit.setText(selectedCache.getCacheAuthor());

        textToEdit = requireView().findViewById(R.id.detailDiffEdit);
        textToEdit.setText(String.format("%d / 5", selectedCache.getCacheDifficulty()));

        textToEdit = requireView().findViewById(R.id.detailTerrEdit);
        textToEdit.setText(String.format("%d / 5", selectedCache.getTerrainDifficulty()));

        textToEdit = requireView().findViewById(R.id.detailSizeEdit);
        textToEdit.setText(selectedCache.getCacheSizeString());

        // set cache comments
        lv = requireView().findViewById(R.id.detailCommentListView);
        CommentListViewAdapter listViewAdapter = new CommentListViewAdapter(this.getContext(), items);
        lv.setAdapter(listViewAdapter);

        // clear text
        EditText commentEditText = requireView().findViewById(R.id.detailCommentEditTextTextMultiLine);
        commentEditText.setText("");

        RadioButton fiveRating = requireView().findViewById(R.id.ratingsFiveRadioButton);
        fiveRating.setChecked(true);

        Button deleteButton = requireView().findViewById(R.id.cacheDeleteButton);
        deleteButton.setVisibility(View.INVISIBLE);
        if (selectedCache.getCacheAuthor().equals("Jesse")) { // TODO: Needs to use signed in username
            deleteButton.setVisibility(View.VISIBLE);
        }


        Button commentButton = requireView().findViewById(R.id.detailCreateCommentbutton);
        commentButton.setClickable(true);
        commentButton.setText("Comment and Rate");
        if (alreadyRated) {
            commentButton.setClickable(false);
            commentButton.setText("Already Rated");
        }

        this.currentGeocacheID = cacheID;
    }
}