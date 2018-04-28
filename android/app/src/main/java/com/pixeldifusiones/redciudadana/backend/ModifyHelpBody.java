package com.pixeldifusiones.redciudadana.backend;

import com.google.gson.annotations.SerializedName;

/**
 * Created by daniel.streitenberger on 19/04/2017.
 */
public class ModifyHelpBody {
    public static final int STATUS_CANCEL = 0;
    public static final int STATUS_ACTIVE = 1;
    public static final int STATUS_TAKEN = 2;
    public static final int STATUS_COMPLETE = 3;
    public static final int STATUS_EXPIRED = 4;
    public static final int STATUS_FALSE_ALARM = 5;

    @SerializedName("email")
    String email;
    @SerializedName("status")
    int status;

    public ModifyHelpBody(String email, int status) {
        this.email = email;
        this.status = status;
    }
}
