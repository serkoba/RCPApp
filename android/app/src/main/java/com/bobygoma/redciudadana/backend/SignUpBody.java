package com.bobygoma.redciudadana.backend;

import com.google.gson.annotations.SerializedName;

/**
 * Created by daniel.streitenberger on 09/03/2017.
 */
public class SignUpBody {

    @SerializedName("firstName")
    String firstName;

    @SerializedName("lastName")
    String lastName;

    @SerializedName("email")
    String email;

    @SerializedName("phone")
    String phone;

    /*@SerializedName("lastCPRTrainingDate")
    String lastCPRTrainingDate;*/

    @SerializedName("address")
    String address;

    @SerializedName("password")
    String password;

    @SerializedName("password_confirmation")
    String passwordConfirmation;

    public SignUpBody(String firstName, String lastName, String email, String phone, String address, String password, String passwordConfirmation) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.password = password;
        this.passwordConfirmation = passwordConfirmation;
    }
}
