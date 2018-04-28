package com.pixeldifusiones.redciudadana.backend;

import com.google.gson.annotations.SerializedName;

/**
 * Created by daniel.streitenberger on 09/03/2017.
 */
public class LocationBody {
    @SerializedName("location")
    Location location;

    public LocationBody(double latitude, double longitude) {
        this.location = new Location(latitude, longitude);
    }
}
