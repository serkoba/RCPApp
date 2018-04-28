package com.pixeldifusiones.redciudadana.backend;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by daniel.streitenberger on 16/04/2017.
 */
public class EmergenciesResponse {

    @SerializedName("emergencies")
    ArrayList<EmergencyNotification> emergencies;

    public ArrayList<EmergencyNotification> getEmergencies() {
        return emergencies;
    }
}
