package com.bobygoma.redciudadana.backend;

import com.google.gson.annotations.SerializedName;

/**
 * Created by daniel.streitenberger on 18/03/2017.
 */
public class FCMTokenBody {

    @SerializedName("fcmToken")
    String fcmToken;

    public FCMTokenBody(String fcmToken) {
        this.fcmToken = fcmToken;
    }
}
