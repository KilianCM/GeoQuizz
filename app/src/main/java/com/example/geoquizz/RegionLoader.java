package com.example.geoquizz;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;

public class RegionLoader extends AsyncTaskLoader<String> {


    RegionLoader(Context context) {
        super(context);
    }


    @Nullable
    @Override
    public String loadInBackground() {
        return NetworkUtils.getAllRegions();
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();

    }
}
