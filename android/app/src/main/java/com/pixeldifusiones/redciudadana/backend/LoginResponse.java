package com.pixeldifusiones.redciudadana.backend;

import com.google.gson.annotations.SerializedName;

/**
 * Created by daniel.streitenberger on 14/02/2017.
 */
public class LoginResponse {

    @SerializedName("token")
    String token;

    public LoginResponse(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}
