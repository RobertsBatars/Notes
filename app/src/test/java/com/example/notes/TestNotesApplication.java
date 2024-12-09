package com.example.notes;

import android.app.Application;
import android.content.Context;
import androidx.appcompat.app.AppCompatDelegate;

public class TestNotesApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        setTheme(android.R.style.Theme_Material_Light);
    }

    @Override
    public void setTheme(int resid) {
        super.setTheme(android.R.style.Theme_Material_Light);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }
}