package com.pixeldifusiones.redciudadana.backend;

import com.google.gson.annotations.SerializedName;

/**
 * Created by daniel.streitenberger on 21/03/2017.
 */
public class AskForHelpBody {

    @SerializedName("location")
    Location location;

    @SerializedName("emergencyType")
    int emergencyType;

    @SerializedName("address")
    String address;

    public AskForHelpBody(double latitude, double longitude, int emergencyType, String address) {
        this.location = new Location(latitude, longitude);
        this.emergencyType = emergencyType;
        this.address = address;
    }
}
