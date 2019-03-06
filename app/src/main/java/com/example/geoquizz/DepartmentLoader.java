package com.example.geoquizz;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;

public class DepartmentLoader extends AsyncTaskLoader<String> {


    DepartmentLoader(Context context) {
        super(context);
    }


    @Nullable
    @Override
    public String loadInBackground() {
        return NetworkUtils.getAllDepartments();
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();

    }
}
