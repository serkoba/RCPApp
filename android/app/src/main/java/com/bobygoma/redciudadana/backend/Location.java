package com.bobygoma.redciudadana.backend;

import com.google.gson.annotations.SerializedName;

/**
 * Created by daniel.streitenberger on 20/03/2017.
 */
public class Location {
    @SerializedName("latitude")
    double latitude;

    @SerializedName("longitude")
    double longitude;

    public Location(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
