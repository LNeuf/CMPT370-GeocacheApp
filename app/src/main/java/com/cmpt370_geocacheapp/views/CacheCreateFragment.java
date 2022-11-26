package com.cmpt370_geocacheapp.views;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Toast;

import com.cmpt370_geocacheapp.controller.ApplicationController;
import com.cmpt370_geocacheapp.model.ApplicationModel;
import com.cmpt370_geocacheapp.imodel.InteractionModel;
import com.cmpt370_geocacheapp.R;
import com.cmpt370_geocacheapp.model.User;

import java.io.IOException;
import java.io.InputStream;

public class CacheCreateFragment extends Fragment {

    ApplicationController controller;
    ApplicationModel model;
    InteractionModel iModel;

    int SELECT_PIC = 200;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Initialize view
        View view = inflater.inflate(R.layout.fragment_cache_create, container, false);

        // event handling
        view.findViewById(R.id.latitudeEditText).setOnLongClickListener(this::enterCurrentLatitude);
        view.findViewById(R.id.longitudeEditText).setOnLongClickListener(this::enterCurrentLongitude);
        view.findViewById(R.id.createCacheButton).setOnClickListener(this::createCache);
        view.findViewById(R.id.addImageButton).setOnClickListener(this::selectImage);
        return view;
    }

    private void selectImage(View view) {
        // create an instance of the
        // intent of the type image
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);

        // pass the constant to compare it
        // with the returned requestCode
        startActivityForResult(Intent.createChooser(i, "Select Picture"), SELECT_PIC);
    }

    // this function is triggered when user
    // selects the image from the imageChooser
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            // compare the resultCode with the
            // SELECT_PICTURE constant
            if (requestCode == SELECT_PIC) {
                // Get the url of the image from data
                Uri selectedImageUri = data.getData();
                if (null != selectedImageUri) {
                    // update the preview image in the layout
                    try {
                        iModel.setLoadedPicture(getSmallImage(selectedImageUri));
                        Toast.makeText(this.getContext(), "Picture added!", Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }
        }
    }

    /**
     * Takes the URI and returns a bitmap - down-sampled if the image is too large
     */
    public Bitmap getSmallImage(Uri uri) throws IOException {
        InputStream in = this.getContext().getContentResolver().openInputStream(uri);

        BitmapFactory.Options boundsOptions = new BitmapFactory.Options();
        boundsOptions.inJustDecodeBounds = true;
        boundsOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;
        BitmapFactory.decodeStream(in, null, boundsOptions);
        in.close();

        if ((boundsOptions.outWidth == -1) || (boundsOptions.outHeight == -1)) {
            return null;
        }

        int originalMaxSize = Math.max(boundsOptions.outHeight, boundsOptions.outWidth);

        double ratio = (originalMaxSize > 500.0) ? (originalMaxSize / 500.0) : 1.0;

        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        bitmapOptions.inSampleSize = calculateSampleSize(ratio);
        bitmapOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;
        in = this.getContext().getContentResolver().openInputStream(uri);
        Bitmap resizedBitmap = BitmapFactory.decodeStream(in, null, bitmapOptions);
        in.close();
        return resizedBitmap;
    }

    /**
     * Gets the takes resize ratio and calculates the proper sample size to reduce the input image
     */
    private int calculateSampleSize(double ratio) {
        int power = Integer.highestOneBit( (int) Math.floor(ratio) );
        if (power == 0)
        {
            return 1;
        }
        else
        {
            return power;
        }
    }

    /**
     * Handles the input checking for creating a new cache, makes controller create a cache if all inputs are good
     */
    public void createCache(View view) {

        EditText textInput = requireView().findViewById(R.id.cacheNameEditText);
        String cacheName = textInput.getText().toString();
        if (cacheName.equals("")) {
            Toast.makeText(this.getContext(), "Please enter a cache name", Toast.LENGTH_SHORT).show();
            return;
        }

        textInput = requireView().findViewById(R.id.latitudeEditText);
        float latitude;
        try {
            latitude = Float.parseFloat(textInput.getText().toString());
        } catch (NumberFormatException e) {
            Toast.makeText(this.getContext(), "Improper latitude entered", Toast.LENGTH_SHORT).show();
            return;
        }
        if (latitude < -90 || latitude > 90)
            Toast.makeText(this.getContext(), "Improper latitude entered", Toast.LENGTH_SHORT).show();

        textInput = requireView().findViewById(R.id.longitudeEditText);
        float longitude;
        try {
            longitude = Float.parseFloat(textInput.getText().toString());
        } catch (NumberFormatException e) {
            Toast.makeText(this.getContext(), "Improper longitude entered", Toast.LENGTH_SHORT).show();
            return;
        }
        if (longitude < -180 || longitude > 180)
            Toast.makeText(this.getContext(), "Improper longitude entered", Toast.LENGTH_SHORT).show();

        int cacheSize = -1;
        RadioGroup group = requireView().findViewById(R.id.cacheSizeRadioGroup);

        int selectedId = group.getCheckedRadioButtonId();
        if (selectedId == R.id.cacheRadioSizeMicro)
            cacheSize = 1;
        else if (selectedId == R.id.cacheRadioSizeSmall)
            cacheSize = 2;
        else if (selectedId == R.id.cacheRadioSizeRegular)
            cacheSize = 3;
        else if (selectedId == R.id.cacheRadioSizeLarge)
            cacheSize = 4;
        else if (selectedId == R.id.cacheRadioSizeOther)
            cacheSize = 5;

        if (cacheSize == -1) {
            Toast.makeText(this.getContext(), "Invalid cache size", Toast.LENGTH_SHORT).show();
            return;
        }

        SeekBar cacheDifficultySeekBar = requireView().findViewById(R.id.cacheDifficultySeekBar);
        int cacheDifficulty = cacheDifficultySeekBar.getProgress() + 1;

        SeekBar terrainDifficultySeekBar = requireView().findViewById(R.id.terrainDifficultySeekBar);
        int terrainDifficulty = terrainDifficultySeekBar.getProgress() + 1;

        User cacheCreator = new User("Jesse", "TestPass", 123);// TODO: Assign creator name based on who is logged in


        controller.createCache(cacheName, cacheCreator, latitude, longitude, cacheDifficulty, terrainDifficulty, cacheSize, iModel.getLoadedPicture());
        iModel.clearLoadedPicture();
    }

    /**
     * Enters the current locations longitude to the text input
     */
    private boolean enterCurrentLongitude(View view) {
        if (iModel.getCurrentLocation() != null) {
            AppCompatEditText textInput = view.findViewById(R.id.longitudeEditText);
            textInput.setText(String.valueOf(iModel.getCurrentLocation().getLongitude()));
        }
        return true;
    }

    /**
     * Enters the current locations latitude to the text input
     */
    private boolean enterCurrentLatitude(View view) {
        if (iModel.getCurrentLocation() != null) {
            AppCompatEditText textInput = view.findViewById(R.id.latitudeEditText);
            textInput.setText(String.valueOf(iModel.getCurrentLocation().getLatitude()));
        }
        return true;
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