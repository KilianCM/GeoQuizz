package com.example.geoquizz;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String> {

    private static final String LOG_TAG = MainActivity.class.getSimpleName() ;

    public static ArrayList<Department> mDepartmentsData;
    public static ArrayList<Region> mRegionsData;

    private int loader1 = 1;
    private int loader2 = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);

        // Initialize the ArrayList that will contain the data.
        mDepartmentsData = new ArrayList<>();
        mRegionsData = new ArrayList<>();


        callApiForDepartments();
        callApiForRegions();


        final Button buttonPlay = (Button) findViewById(R.id.button_play_text);
        buttonPlay.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                launchQuizz(view);
            }
        });

        final Button buttonScore = (Button) findViewById(R.id.button_trophy_text);
        buttonScore.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                displayScore(view);
            }
        });

        final Button buttonSearch = (Button) findViewById(R.id.button_search_text);
        buttonSearch.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                displayMap(view);
            }
        });
    }

    public void launchQuizz(View view) {
        Intent intent = new Intent(this, QuizzGeolocalisation.class);
        intent.putExtra("QUIZZ_TYPE",0);
        startActivity(intent);
    }

    public void displayScore(View view) {
        Intent intent = new Intent(this, ScoreActivity.class);
        startActivity(intent);
    }

    public void displayMap(View view) {
        Intent intent = new Intent(this, CitySelection.class);
        startActivity(intent);
    }

    public void callApiForDepartments(){
        // Check the status of the network connection.
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = null;
        if (connMgr != null) {
            networkInfo = connMgr.getActiveNetworkInfo();
        }

        // If the network is available, connected, and the search fields
        // is not empty, start a CityLoader AsyncTask.
        if (networkInfo != null && networkInfo.isConnected()) {

            Bundle queryBundle = new Bundle();
            getSupportLoaderManager().restartLoader(loader1, queryBundle, this);

        }

        else {
        }
    }

    public void callApiForRegions(){
        // Check the status of the network connection.
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = null;
        if (connMgr != null) {
            networkInfo = connMgr.getActiveNetworkInfo();
        }

        // If the network is available, connected, and the search fields
        // is not empty, start a CityLoader AsyncTask.
        if (networkInfo != null && networkInfo.isConnected()) {

            Bundle queryBundle = new Bundle();
            getSupportLoaderManager().restartLoader(loader2, queryBundle, this);

        }

        else {
        }
    }



    @Override
    public Loader<String> onCreateLoader(int id, @Nullable Bundle args) {
        if (id == loader1 ) {
            return new DepartmentLoader(this);
        }
        else if(id == loader2){
            return new RegionLoader(this);
        }
        else {
            return null;
        }
    }

    @Override
    public void onLoadFinished(@NonNull Loader<String> loader, String data) {
        int id = loader.getId();// find which loader you called
        if (id == loader1 ) {

            try {
                // Convert the response into a JSON object.
                //JSONObject jsonObject = new JSONObject(data);


                // Get the JSONArray of city items.
                //JSONArray itemsArray = jsonObject.getJSONArray("items");
                JSONArray itemsArray = new JSONArray(data);

                // Initialize iterator and results fields.
                int i = 0;


                // Look for results in the items array, exiting when both the
                // title and author are found or when all items have been checked.
                while (i < itemsArray.length()) {
                    // Get the current item information.
                    JSONObject department = itemsArray.getJSONObject(i);
                    // Try to get the name of the city,
                    // catch if either field is empty and move on.
                    try {
                        mDepartmentsData.add(new Department(department.getString("nom"), department.getString("code")));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    // Move to the next item.
                    i++;
                }

            } catch (Exception e) {
                // If onPostExecute does not receive a proper JSON string,
                // update the UI to show failed results.
                e.printStackTrace();
            }
        }
        else if(id == loader2){
            try {
                // Convert the response into a JSON object.
                //JSONObject jsonObject = new JSONObject(data);


                // Get the JSONArray of city items.
                //JSONArray itemsArray = jsonObject.getJSONArray("items");
                JSONArray itemsArray = new JSONArray(data);

                // Initialize iterator and results fields.
                int i = 0;


                // Look for results in the items array, exiting when both the
                // title and author are found or when all items have been checked.
                while (i < itemsArray.length()) {
                    // Get the current item information.
                    JSONObject region = itemsArray.getJSONObject(i);
                    // Try to get the name of the city,
                    // catch if either field is empty and move on.
                    try {
                        mRegionsData.add(new Region(region.getString("nom"), region.getString("code")));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    // Move to the next item.
                    i++;
                }

            } catch (Exception e) {
                // If onPostExecute does not receive a proper JSON string,
                // update the UI to show failed results.
                e.printStackTrace();
            }
        }

    }

    @Override
    public void onLoaderReset(@NonNull Loader<String> loader) {

    }
}
