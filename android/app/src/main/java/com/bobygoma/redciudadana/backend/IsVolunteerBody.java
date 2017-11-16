package com.bobygoma.redciudadana.backend;

import com.google.gson.annotations.SerializedName;

/**
 * Created by daniel.streitenberger on 22/03/2017.
 */
public class IsVolunteerBody {

    @SerializedName("isVolunteer")
    int isVolunteer;

    @SerializedName("radio")
    int radio;

    @SerializedName("avail_start")
    int availableStart;

    @SerializedName("avail_end")
    int availableEnd;

    public IsVolunteerBody(int isVolunteer, int radio, int availableStart, int availableEnd) {
        this.isVolunteer = isVolunteer;
        this.radio = radio;
        this.availableStart = availableStart;
        this.availableEnd = availableEnd;
    }
}
