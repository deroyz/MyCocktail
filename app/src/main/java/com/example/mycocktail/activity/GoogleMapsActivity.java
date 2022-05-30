package com.example.mycocktail.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.mycocktail.R;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.PlaceLikelihood;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
import com.google.android.libraries.places.api.net.FindCurrentPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import java.util.Arrays;
import java.util.List;


public class GoogleMapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final String LOG_TAG = GoogleMapsActivity.class.getSimpleName();

    private static final String MAPS_API_KEY = "AIzaSyDH1Y44gLQuklSsHNhnZEG1557L8tvKQDc";

    private PlacesClient mPlacesClient;

    private FragmentManager fragmentManager;
    private MapFragment mapFragment;

    private Button mButton;

    private ActivityResultLauncher<String[]> requestPermissionLauncher;

    private String PERMISSIONS[] = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };

    private final int PERMISSION_REQUEST_CODE = 101;
    private LatLng mCurrentLatLng;
    private Place mCurrentPlace;
    private GoogleMap mGoogleMap;

    public static final String PLACE_ID = "placeID";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_googlemap);

        mButton = findViewById(R.id.button);

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent returnIntent = new Intent();
                returnIntent.putExtra(PLACE_ID, mCurrentPlace.getId());
                setResult(Activity.RESULT_OK, returnIntent);
                finish();

            }
        });

        setupPermissionLauncher();

        askPermissions(requestPermissionLauncher);

        getGoogleMapFragment();
        setupGooglePlace();
        getAutoCompleteFragment();
        findCurrentPlace();

    }

    private void setupPermissionLauncher() {

        requestPermissionLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestMultiplePermissions(), isGranted -> {

                    Log.e(LOG_TAG, "Launcher result: " + isGranted.toString());

                    if (isGranted.containsValue(false)) {
                        Log.e(LOG_TAG, "At least one of the permissions was not granted, launching again...");
                        requestPermissionLauncher.launch(PERMISSIONS);
                    }
                });
    }

    private void askPermissions(ActivityResultLauncher<String[]> requestPermissionLauncher) {

        if (!hasPermission(PERMISSIONS)) {

            Log.e(LOG_TAG, "Launching multiple contract permission launcher for all required permissions");

            ActivityCompat.requestPermissions(GoogleMapsActivity.this, PERMISSIONS, PERMISSION_REQUEST_CODE);

        } else {
            Log.e(LOG_TAG, "All permissions are already granted");
        }

    }

    private boolean hasPermission(String[] permissions) {

        if (permissions != null) {
            for (String permission : permissions) {

                if (ContextCompat.checkSelfPermission(this, permission)
                        == PackageManager.PERMISSION_DENIED) {
                    Log.e(LOG_TAG, "Permission is not granted: " + "permission");
                    return false;

                } else {
                    Log.e(LOG_TAG, "Permission is granted: " + "permission");
                    return true;
                }

            }
        }

        return false;

    }

    private void findCurrentPlace() {

        Log.e(LOG_TAG, "getCurrentPlace");

        // Use fields to define the data types to return
        List<Place.Field> placeFields = Arrays.asList(Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG);

        //Use the builder to create a FindCurrentPlaceRequest
        FindCurrentPlaceRequest request = FindCurrentPlaceRequest.newInstance(placeFields);

        //Call findCurrentPlace and handle the response (first check that the user has granted permission)
        if (hasPermission(PERMISSIONS)) {

            @SuppressLint("MissingPermission")
            Task<FindCurrentPlaceResponse> placeResponse = mPlacesClient.findCurrentPlace(request);

            placeResponse.addOnCompleteListener(task -> {

                if (task.isSuccessful()) {

                    FindCurrentPlaceResponse response = task.getResult();

                    for (PlaceLikelihood placeLikelihood : response.getPlaceLikelihoods()) {
                        Log.e(LOG_TAG, String.format("Place '%s' has likelihood: %f",
                                placeLikelihood.getPlace().getName(),
                                placeLikelihood.getLikelihood()));
                    }

                } else {

                    Exception exception = task.getException();
                    if (exception instanceof ApiException) {
                        ApiException apiException = (ApiException) exception;
                        Log.e(LOG_TAG, "Place not found: " + apiException.getStatusCode());

                    }
                }
            });

        } else {

            // Method to ask required permissions;
            askPermissions(requestPermissionLauncher);

        }
    }

    private void fetchPlace() {


    }

    private void setupGooglePlace() {

        Log.e(LOG_TAG, "initGooglePlace");

        // Initialize the SDK
        Places.initialize(getApplicationContext(), MAPS_API_KEY);

        // Create a new PlacesClient instance
        PlacesClient placesClient = Places.createClient(this);

        mPlacesClient = placesClient;

    }

    private void getAutoCompleteFragment() {

        Log.e(LOG_TAG, "setupAutoCompleteFragment");

        // Initialize the AutocompleteSupportFragment.
        AutocompleteSupportFragment autocompleteFragment
                = (AutocompleteSupportFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_auto_complete);

        //Specify the types of place data to return
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG));

        //Bias results to a specific region
        autocompleteFragment.setLocationBias(RectangularBounds.newInstance(
                new LatLng(-33.880490, 151.184363),
                new LatLng(-33.858754, 151.229596)));
        //Setup filter
        autocompleteFragment.setTypeFilter(TypeFilter.ESTABLISHMENT);
        autocompleteFragment.setTypeFilter(TypeFilter.ADDRESS);


        //Set up a PlaceSelectionListener to handle the response
        autocompleteFragment.setOnPlaceSelectedListener((new PlaceSelectionListener() {
            @Override
            public void onError(@NonNull Status status) {
                //  TODO: Handle the error.
                Log.e(LOG_TAG, "An error occurred: " + status);
            }

            @Override
            public void onPlaceSelected(@NonNull Place place) {
                //  TODO: Get info about the selected place
                Log.e(LOG_TAG, "Place: " + place.getName() + ", " + place.getId());

                mCurrentPlace = place;
                mCurrentLatLng = place.getLatLng();

                Log.e(LOG_TAG, "LatLng: " + place.getName() + ", " + mCurrentLatLng);

                if (mCurrentLatLng!=null) {

                    mGoogleMap.addMarker(new MarkerOptions()
                            .position(mCurrentLatLng)
                            .title("Marker in Sydney"));
                    mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(mCurrentLatLng));

                }
            }
        }));
    }

    private void getGoogleMapFragment() {

        Log.e(LOG_TAG, "getGoogleMapFragment");

        fragmentManager = getFragmentManager();

        mapFragment = (MapFragment) fragmentManager.findFragmentById(R.id.fragment_google_map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

        Log.e(LOG_TAG, "onMapReady");

        LatLng location = new LatLng(37.485284, 126.901451);

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.title("Guro");
        markerOptions.snippet("Metro Station");
        markerOptions.position(location);

        googleMap.addMarker(markerOptions);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15));
        mGoogleMap = googleMap;


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finish();

    }
}
