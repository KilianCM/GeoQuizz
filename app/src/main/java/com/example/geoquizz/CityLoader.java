package com.example.geoquizz;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;

public class CityLoader extends AsyncTaskLoader<String> {

    private Double mLongitude;
    private Double mLatitude;


    CityLoader(Context context, Double longitude, Double latitude) {
        super(context);
        mLatitude = latitude;
        mLongitude = longitude;
    }


    @Nullable
    @Override
    public String loadInBackground() {
        return NetworkUtils.getCityInfoWithLocalisation(mLongitude,mLatitude);
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();

    }

}
