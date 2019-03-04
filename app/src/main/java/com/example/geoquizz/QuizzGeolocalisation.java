package com.example.geoquizz;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;



import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class QuizzGeolocalisation extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<String>, ActivityCompat.OnRequestPermissionsResultCallback {

    private TextView mNameText;
    private City mCity = new City();

    private static final String LOG_TAG =
            QuizzGeolocalisation.class.getSimpleName();

    //Identifiant de la demande de permission
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    //Si l'utilisateur à autoriser la localisation ou non
    private Boolean mLocationPermissionGranted = false;
    //Dernière position détéctée
    private Location mLastKnowLocation;
    //Permet de récuperer la position
    private FusedLocationProviderClient mFusedLocationProviderClient;

    private Double mLongitude;
    private Double mLatitude;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quizz_geolocalisation);

        //mTitleText = findViewById(R.id.titleText);
        mNameText = findViewById(R.id.city_name);

        if (getSupportLoaderManager().getLoader(0) != null) {
            getSupportLoaderManager().initLoader(0, null, this);
        }

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        this.getLocationPermission();
        this.getDeviceLocation();
    }

    public void callApi(Double latitude, Double longitude){
        // Check the status of the network connection.
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = null;
        if (connMgr != null) {
            networkInfo = connMgr.getActiveNetworkInfo();
        }

        // If the network is available, connected, and the search fields
        // is not empty, start a CityLoader AsyncTask.
        if (networkInfo != null && networkInfo.isConnected()
                && latitude != null && longitude != null) {

            Bundle queryBundle = new Bundle();
            queryBundle.putDouble("longitude", mLongitude);
            queryBundle.putDouble("latitude", mLatitude);
            getSupportLoaderManager().restartLoader(0, queryBundle, this);

        }

        else {
            if (latitude == null || longitude == null) {
                Log.d(LOG_TAG,"Valeurs nulles");

            }
        }
    }


    @Override
    public Loader<String> onCreateLoader(int id, @Nullable Bundle args) {
        Double longitude = null;
        Double latitude = null;

        if (args != null) {
            longitude = args.getDouble("longitude");
            latitude = args.getDouble("latitude");
        }

        return new CityLoader(this, longitude,latitude);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<String> loader, String data) {

        try {
            // Convert the response into a JSON object.
            //JSONObject jsonObject = new JSONObject(data);



            // Get the JSONArray of city items.
            //JSONArray itemsArray = jsonObject.getJSONArray("items");
            JSONArray itemsArray = new JSONArray(data);
            Log.d("NetworkUtils",itemsArray.toString());

            // Initialize iterator and results fields.
            int i = 0;
            String name = null;

            // Look for results in the items array, exiting when both the
            // title and author are found or when all items have been checked.
            while (i < itemsArray.length() &&
                    (name == null)) {
                // Get the current item information.
                JSONObject city = itemsArray.getJSONObject(i);

                // Try to get the name of the city,
                // catch if either field is empty and move on.
                try {
                    name = city.getString("nom");
                    mCity.setName(city.getString("nom"));
                    mCity.setCode(city.getString("code"));
                    mCity.setCodeDepartment(city.getString("codeDepartement"));
                    mCity.setCodeRegion(city.getString("codeRegion"));
                    mCity.setSurface(city.getDouble("surface")/100);
                    mCity.setPopulation(city.getString("population"));
                    mCity.setRegion(city.getJSONObject("region"));
                    mCity.setDepartment(city.getJSONObject("departement"));

                    //TO DO : arrondir la valeur de la surface
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                // Move to the next item.
                i++;
            }

            // If both are found, display the result.
            if (name != null) {
                mNameText.setText(name);
            } else {
                // If none are found, update the UI to show failed results.
                mNameText.setText("rien");
            }

        } catch (Exception e) {
            // If onPostExecute does not receive a proper JSON string,
            // update the UI to show failed results.
            mNameText.setText("error");
            e.printStackTrace();
        }

    }

    @Override
    public void onLoaderReset(@NonNull Loader<String> loader) {

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

    private void getDeviceLocation() {
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
                            mLongitude = mLastKnowLocation.getLongitude();
                            mLatitude = mLastKnowLocation.getLatitude();
                            callApi(mLatitude, mLongitude);
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

}
