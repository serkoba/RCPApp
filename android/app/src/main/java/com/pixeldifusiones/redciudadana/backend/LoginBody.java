package com.pixeldifusiones.redciudadana.backend;

import com.google.gson.annotations.SerializedName;

/**
 * Created by daniel.streitenberger on 14/02/2017.
 */
public class LoginBody {

    @SerializedName("email")
    String email;

    @SerializedName("password")
    String password;

    public LoginBody(String email, String password){
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
