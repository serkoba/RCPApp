package com.pixeldifusiones.agromercado.backend;

import com.google.gson.annotations.SerializedName;

/**
 * Created by daniel.streitenberger on 19/02/2017.
 */
public class LogoutBody {
    @SerializedName("token")
    String token;

    public LogoutBody(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}
