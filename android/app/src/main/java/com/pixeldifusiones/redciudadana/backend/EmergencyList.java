package com.pixeldifusiones.redciudadana.backend;

import java.util.ArrayList;

/**
 * Created by daniel.streitenberger on 22/03/2017.
 */
public class EmergencyList {

    ArrayList<EmergencyNotification> emergencies;

    public EmergencyList(ArrayList<EmergencyNotification> providers) {
        this.emergencies = providers;
    }

    public ArrayList<EmergencyNotification> getEmergencies() {
        return emergencies;
    }

    public void setEmergencies(ArrayList<EmergencyNotification> providers) {
        this.emergencies = providers;
    }
}
