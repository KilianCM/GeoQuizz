package com.example.geoquizz;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CitySelection extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Place mPlace;
    AutocompleteSupportFragment placeAutoComplete;

    //Identifiant de la demande de permission
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    //Si l'utilisateur à autoriser la localisation ou non
    private Boolean mLocationPermissionGranted = false;
    //Dernière position détéctée
    private Location mLastKnowLocation;
    //Permet de récuperer la position
    private FusedLocationProviderClient mFusedLocationProviderClient;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quizz_map);

        Places.initialize(getApplicationContext(), getApplicationContext().getString(R.string.google_maps_key));
        PlacesClient placesClient = Places.createClient(this);

        placeAutoComplete = (AutocompleteSupportFragment) getSupportFragmentManager().findFragmentById(R.id.place_autocomplete);
        placeAutoComplete.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.ADDRESS));
        placeAutoComplete.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                mPlace = place;
                Log.d("Maps", "Place selected: " + place.getName() + " - adresse " + place.getAddress() + " - latlng " + place.getLatLng());
                addMarker(place);
            }

            @Override
            public void onError(Status status) {
                Log.d("Maps", "An error occurred: " + status);

            }
        });

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        final Context context = this;

        final Button buttonPlay = (Button) findViewById(R.id.button_launch);
        buttonPlay.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if(mPlace != null){
                    if(mPlace.getLatLng() != null) {
                        launchQuizz(view, mPlace.getLatLng().latitude, mPlace.getLatLng().longitude);
                    }
                } else {
                    new AlertDialog.Builder(context)
                            .setTitle("Attention")
                            .setMessage("Veuillez rechercher une commune !")

                            // A null listener allows the button to dismiss the dialog and take no further action.
                            .setNegativeButton(android.R.string.ok, null)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }

            }
        });

    }

    public void launchQuizz(View view, Double lat, Double lon) {
        Intent intent = new Intent(this, QuizzGeolocalisation.class);
        intent.putExtra("QUIZZ_TYPE",1);
        intent.putExtra("LAT",lat);
        intent.putExtra("LON",lon);
        startActivity(intent);
    }

    private void centerMapOnMyLocation() {
        try {
            //On Check toujours si on a le droit, sinon ca plante.
            if(mLocationPermissionGranted){
                //On demande à notre service de localisation de trouver la position actuelle
                @SuppressLint("MissingPermission") Task locationResult = mFusedLocationProviderClient.getLastLocation();
                //Puisque c'est de l'asynchrone. On ajoute un écouteur qui nous préviens quand la detection est finie.
                //On ne sait jamais si le GPS marche, si il est lent #wiko, le temps de match des satellites.
                // De plus la position peut-etre déduite par pléthore de systèmes : Wifi, Bluetooth, Antenne LTE, etc.)
                locationResult.addOnCompleteListener(this, new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        //Une fois la detection effectuée, on vérifie que tout est OK
                        if(task.isSuccessful()){
                            //Si oui on attribue la dernière localisation à notre variable pour la garder dans un coin.
                            mLastKnowLocation = (Location) task.getResult();
                            LatLng coordinates = new LatLng(mLastKnowLocation.getLatitude(), mLastKnowLocation.getLongitude());
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(coordinates, 13));

                            CameraPosition cameraPosition = new CameraPosition.Builder()
                                    .target(coordinates)      // Sets the center of the map to location user
                                    .zoom(10)                   // Sets the zoom
                                    .build();                   // Creates a CameraPosition from the builder
                            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                        }else {
                            //Traitement du cas où on ne trouve pas de position.
                            Log.d("TAG", "Current location is null. Using defaults.");
                            Log.e("TAG", "Exception: %s", task.getException());
                        }
                    }
                });

            }
        } catch (SecurityException e){
            Log.e("TAG", e.getMessage());
        }
    }

    //On reçoit la réponse de l'utilisateur ici
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                    this.centerMapOnMyLocation();
                }
                else{
                    Toast.makeText(CitySelection.this, "Impossible de centrer sur ta position sans autorisation de localisation ! :(", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    //On demande la permission ici
    protected void getLocationPermission(){
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    public void addMarker(Place p){

        MarkerOptions markerOptions = new MarkerOptions();

        markerOptions.position(p.getLatLng());
        markerOptions.title(p.getName()+"");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));

        mMap.clear();
        mMap.addMarker(markerOptions);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(p.getLatLng()));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(10));

    }

    @Override
    public void onMapReady(GoogleMap map) {

        mMap = map;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        this.getLocationPermission();
        centerMapOnMyLocation();
    }

    public void onMapSearch(View view) {
        //EditText locationSearch = (EditText) findViewById(R.id.editText);
        /*String location = locationSearch.getText().toString();
        List<Address> addressList = null;
        mMap.clear();
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(0, 0))
                .title("Marker"));

        if (location != null || !location.equals("")) {
            Geocoder geocoder = new Geocoder(this);
            try {
                addressList = geocoder.getFromLocationName(location, 1);

            } catch (IOException e) {
                e.printStackTrace();
            }

            Address address = addressList.get(0);
            LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());

            mMap.addMarker(new MarkerOptions().position(latLng).title("Marker"));
            mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
        }*/
    }

}
