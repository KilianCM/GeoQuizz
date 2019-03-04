package com.example.geoquizz;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class QuizzGeolocalisation extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<String> {

    private TextView mNameText;
    private City mCity = new City();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quizz_geolocalisation);

        //mTitleText = findViewById(R.id.titleText);
        mNameText = findViewById(R.id.text_city);

        if (getSupportLoaderManager().getLoader(0) != null) {
            getSupportLoaderManager().initLoader(0, null, this);
        }

        // TO DO : Get the coordinates.
        Float latitude = 45f;
        Float longitude = 6f;


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
            queryBundle.putFloat("longitude", longitude);
            queryBundle.putFloat("latitude", latitude);
            getSupportLoaderManager().restartLoader(0, queryBundle, this);

            /*mAuthorText.setText("");
            mTitleText.setText(R.string.loading);*/
        }

        else {
            if (longitude == null || latitude == null) {
                //TO DO : Popup d'erreur
            } else {

            }
        }


    }


    @Override
    public Loader<String> onCreateLoader(int id, @Nullable Bundle args) {
        Float longitude = null;
        Float latitude = null;

        if (args != null) {
            longitude = args.getFloat("longitude");
            latitude = args.getFloat("latitude");
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
                    mCity.setSurface(city.getDouble("surface"));
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

}
