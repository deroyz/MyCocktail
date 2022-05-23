package com.example.mycocktail.activity;

import android.app.FragmentManager;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mycocktail.R;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import java.util.Arrays;


public class SearchActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final String LOG_TAG = SearchActivity.class.getSimpleName();

    private static final String MAPS_API_KEY = "AIzaSyDH1Y44gLQuklSsHNhnZEG1557L8tvKQDc";

    private FragmentManager fragmentManager;
    private MapFragment mapFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        getGoogleMapFragment();

        getGooglePlaceFragment();

    }

    private void getGooglePlaceFragment() {
        // Initialize the SDK
        Places.initialize(getApplicationContext(), MAPS_API_KEY);

        // Create a new PlacesClient instance
        PlacesClient placesClient = Places.createClient(this);

        // Initialize the AutocompleteSupportFragment.
        AutocompleteSupportFragment autocompleteFragment
                = (AutocompleteSupportFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_auto_complete);

        //Specify the types of place data to return
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME));

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
            }
        }));
    }

    private void getGoogleMapFragment() {
        fragmentManager = getFragmentManager();
        mapFragment = (MapFragment) fragmentManager.findFragmentById(R.id.fragment_google_map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

        LatLng location = new LatLng(37.485284, 126.901451);
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.title("Guro");
        markerOptions.snippet("Metro Station");
        markerOptions.position(location);
        googleMap.addMarker(markerOptions);

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15));


    }
}
