package com.example.geoquizz;

import android.net.Uri;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;

public class NetworkUtils {
    private static final String LOG_TAG =
            NetworkUtils.class.getSimpleName();

    // Base URL for Books API.
    private static final String GEOAPI_BASE_URL =  "https://geo.api.gouv.fr/communes?";
    // Parameter for the search string.
    private static final String LON_PARAM = "lon";
    // Parameter for the search string.
    private static final String LAT_PARAM = "lat";
    // Parameter for returned fields.
    private static final String FIELDS_PARAM = "fields";
    // Parameter for the return format.
    private static final String RETURN_FORMAT = "format";
    // Parameter for the return.
    private static final String GEOMETRY_PARAM = "geometry";


    static String getCityInfoWithLocalisation(Float longitude, Float latitude){
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String cityJSONString = null;

        try {
            Uri builtURI = Uri.parse(GEOAPI_BASE_URL).buildUpon()
                    .appendQueryParameter(LON_PARAM, longitude.toString())
                    .appendQueryParameter(LAT_PARAM, latitude.toString())
                    .appendQueryParameter(FIELDS_PARAM, "nom,code,codesPostaux,surface,codeDepartement,departement,codeRegion,region,population")
                    .appendQueryParameter(RETURN_FORMAT, "json")
                    .appendQueryParameter(GEOMETRY_PARAM, "centre")
                    .build();

            //URL requestURL = new URL(builtURI.toString());
            URL requestURL = new URL(URLDecoder.decode(builtURI.toString(), "UTF-8"));

            Log.d(LOG_TAG,requestURL.toString());

            urlConnection = (HttpURLConnection) requestURL.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Get the InputStream.
            InputStream inputStream = urlConnection.getInputStream();

            // Create a buffered reader from that input stream.
            reader = new BufferedReader(new InputStreamReader(inputStream));

            // Use a StringBuilder to hold the incoming response.
            StringBuilder builder = new StringBuilder();

            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
                // Since it's JSON, adding a newline isn't necessary (it won't
                // affect parsing) but it does make debugging a *lot* easier
                // if you print out the completed buffer for debugging.
                builder.append("\n");
            }

            if (builder.length() == 0) {
                // Stream was empty. No point in parsing.
                return null;
            }

            cityJSONString = builder.toString();


        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        Log.d(LOG_TAG,cityJSONString);
        return cityJSONString;
    }

}
