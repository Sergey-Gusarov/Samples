package ru.breffi.androidstoryclmsdk;

import android.app.Application;
import android.content.Context;

/**
 * Created by tselo on 5/15/2017.
 */

public class App extends Application {

    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
    }

    public static Context getContext(){
        return mContext;
    }
}