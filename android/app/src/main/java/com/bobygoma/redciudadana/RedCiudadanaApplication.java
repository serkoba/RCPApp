package com.bobygoma.redciudadana;

import android.location.Location;
import android.support.multidex.MultiDexApplication;

/**
 * Created by daniel.streitenberger on 09/05/2017.
 */

public class RedCiudadanaApplication extends MultiDexApplication {
    /**
     * The desired interval for location updates. Inexact. Updates may be more or less frequent.
     */
    public static final long UPDATE_INTERVAL_IN_MILLISECONDS = 1000 * 60; //1 minute
    /**
     * The fastest rate for active location updates. Exact. Updates will never be more frequent
     * than this value.
     */

    @Override
    public void onCreate() {
        super.onCreate();
    }
}
