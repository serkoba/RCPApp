package com.pixeldifusiones.redciudadana.backend;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;

public interface EndPointInterface {

    @POST("user/new")
    Call<Void> signUp(
            @Body SignUpBody signUpBody
    );

    @POST("login")
    Call<LoginResponse> login(
            @Body LoginBody loginBody
    );

    @POST("logout")
    Call<Void> logout(
    );

    @PATCH("profile")
    Call<Void> updateLocation(
            @Body LocationBody locationBody
    );

    @GET("profile")
    Call<User> getUser(
    );

    @PATCH("profile")
    Call<Void> updateFCMToken(
            @Body FCMTokenBody fcmTokenBody
    );

    @PATCH("profile")
    Call<Void> isVolunteer(
            @Body IsVolunteerBody isVolunteerBody
    );

    @PATCH("profile")
    Call<Void> updateUser(
            @Body User User
    );

    @POST("help")
    Call<AskForHelpResponse> askForHelp(
            @Body AskForHelpBody askForHelpBody
    );

    @PATCH("help/status")
    Call<Void> modifyStatusHelp(
            @Body ModifyHelpBody modifyHelpBody
    );

    @GET("emergencies")
    Call<EmergenciesResponse> getEmergencies(
    );
}
