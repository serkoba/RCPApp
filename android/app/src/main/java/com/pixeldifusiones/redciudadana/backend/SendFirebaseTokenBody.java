package com.pixeldifusiones.redciudadana.backend;

import com.google.gson.annotations.SerializedName;

/**
 * Created by daniel.streitenberger on 14/02/2017.
 */
public class SendFirebaseTokenBody {
    @SerializedName("fcm_token")
    String firebaseToken;

    @SerializedName("token")
    String token;

    public SendFirebaseTokenBody(String firebaseToken, String token) {
        this.firebaseToken = firebaseToken;
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public String getFirebaseToken() {
        return firebaseToken;
    }
}
