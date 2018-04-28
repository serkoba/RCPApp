package com.pixeldifusiones.redciudadana.controllers;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.pixeldifusiones.redciudadana.backend.AskForHelpBody;
import com.pixeldifusiones.redciudadana.backend.AskForHelpResponse;
import com.pixeldifusiones.redciudadana.backend.EmergenciesResponse;
import com.pixeldifusiones.redciudadana.backend.EmergencyNotification;
import com.pixeldifusiones.redciudadana.backend.EndPointService;
import com.pixeldifusiones.redciudadana.backend.FCMTokenBody;
import com.pixeldifusiones.redciudadana.backend.IsVolunteerBody;
import com.pixeldifusiones.redciudadana.backend.LocationBody;
import com.pixeldifusiones.redciudadana.backend.LoginBody;
import com.pixeldifusiones.redciudadana.backend.LoginResponse;
import com.pixeldifusiones.redciudadana.backend.ModifyHelpBody;
import com.pixeldifusiones.redciudadana.backend.SignUpBody;
import com.pixeldifusiones.redciudadana.backend.User;
import com.pixeldifusiones.redciudadana.utils.Commons;
import com.google.gson.Gson;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by daniel.streitenberger on 14/02/2017.
 */

public class AccountController {
    private static AccountController mInstance;

    private EventBus mEventBus;
    private Context mContext;
    private boolean mBusy = false;

    private LoginResponse mLoginResponse;
    private String errorMessage;

    private AskForHelpResponse askForHelpResponse;

    private AccountController(Context context) {
        mContext = context;
        mEventBus = EventBus.builder().build();
    }

    public static AccountController getInstance(Context context) {

        if (mInstance == null) {
            mInstance = new AccountController(context);
        }

        return mInstance;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void signUp(SignUpBody signUpBody) {
        if (!mBusy) {
            mBusy = true;
            mEventBus.post(Commons.SIGN_UP_START);

            EndPointService apiService = EndPointService.getInstance(mContext);
            Callback<Void> callback = new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {

                    mBusy = false;
                    if (response.isSuccessful()) {
                        mEventBus.post(Commons.SIGN_UP_FINISH);
                        Log.d("ACCOUNT CONTROLLER: ", "Sign up successful");
                    } else {
                        mEventBus.post(Commons.SIGN_UP_ERROR);
                        Log.d("ACCOUNT CONTROLLER: ", "Sign up ERROR");
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    errorMessage = t.getMessage();
                    mBusy = false;
                    mEventBus.post(Commons.SIGN_UP_ERROR);
                    Log.d("ACCOUNT CONTROLLER: ", "Sign up ERROR");
                }

            };

            Call<Void> call = apiService.getEndPoint()
                    .signUp(signUpBody);
            call.enqueue(callback);
        }
    }

    public void login(final String email, String password) {
        if (!mBusy) {

            mBusy = true;
            mEventBus.post(Commons.LOGIN_START);

            EndPointService apiService = EndPointService.getInstance(mContext);
            Callback<LoginResponse> callback = new Callback<LoginResponse>() {
                @Override
                public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {

                    mBusy = false;
                    if (response.isSuccessful()) {
                        mLoginResponse = response.body();
                        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(mContext);
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putString(Commons.SHARED_PREFERENCE_TOKEN, mLoginResponse.getToken());
                        editor.putString(Commons.SHARED_PREFERENCE_EMAIL, email);
                        editor.apply();
                        mEventBus.post(Commons.LOGIN_FINISH);
                        Log.d("ACCOUNT CONTROLLER: ", "Login successful");
                    } else {
                        mEventBus.post(Commons.LOGIN_ERROR);
                        Log.d("ACCOUNT CONTROLLER: ", "Login ERROR");
                    }
                }

                @Override
                public void onFailure(Call<LoginResponse> call, Throwable t) {
                    errorMessage = t.getMessage();
                    mBusy = false;
                    mEventBus.post(Commons.LOGIN_ERROR);
                    Log.d("ACCOUNT CONTROLLER: ", "Login ERROR");
                }

            };

            Call<LoginResponse> call = apiService.getEndPoint()
                    .login(new LoginBody(email, password)
                    );
            call.enqueue(callback);
        }
    }

    public void logout() {
        if (!mBusy) {
            mBusy = true;
            mEventBus.post(Commons.LOGOUT_START);

            EndPointService apiService = EndPointService.getInstance(mContext);
            Callback<Void> callback = new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {

                    mBusy = false;
                    if (response.isSuccessful()) {
                        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(mContext);
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putString(Commons.SHARED_PREFERENCE_TOKEN, "");
                        editor.putString(Commons.SHARED_PREFERENCE_EMAIL, "");
                        editor.putString(Commons.SHARED_PREFERENCE_FIRST_NAME, "");
                        editor.putString(Commons.SHARED_PREFERENCE_LAST_NAME, "");
                        editor.putString(Commons.SHARED_PREFERENCE_PHONE, "");
                        editor.putString(Commons.SHARED_PREFERENCE_ADDRESS, "");

                        editor.putBoolean(Commons.SHARED_PREFERENCE_IS_VOLUNTEER, false);
                        editor.putInt(Commons.SHARED_PREFERENCE_IS_VOLUNTEER_FILTERS, 0);
                        editor.putBoolean(Commons.SHARED_PREFERENCE_EMERGENCY_OTHER, false);
                        editor.putBoolean(Commons.SHARED_PREFERENCE_EMERGENCY_HEART_ATTACK, false);
                        editor.putBoolean(Commons.SHARED_PREFERENCE_EMERGENCY_WOUND, false);
                        editor.putBoolean(Commons.SHARED_PREFERENCE_EMERGENCY_FAINT, false);
                        editor.putInt(Commons.SHARED_PREFERENCE_EMERGENCY_RADIO, 2);

                        editor.putString(Commons.SHARED_PREFERENCE_EMERGENCY_CREATED, "");
                        editor.apply();
                        mEventBus.post(Commons.LOGOUT_FINISH);
                        Log.d("ACCOUNT CONTROLLER: ", "Logout successful");
                    } else {
                        mEventBus.post(Commons.LOGIN_ERROR);
                        Log.d("ACCOUNT CONTROLLER: ", "Logout ERROR");
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    errorMessage = t.getMessage();
                    mBusy = false;
                    mEventBus.post(Commons.LOGIN_ERROR);
                    Log.d("ACCOUNT CONTROLLER: ", "Login ERROR");
                }

            };

            Call<Void> call = apiService.getEndPoint().logout();
            call.enqueue(callback);
        }
    }

    public void updateLocation(double lat, double lng) {
        if (!mBusy) {
            mBusy = true;
            mEventBus.post(Commons.LOCATION_UPDATE_START);

            EndPointService apiService = EndPointService.getInstance(mContext);

            Call<Void> call = apiService.getEndPoint().updateLocation(new LocationBody(lat, lng));
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {

                    mBusy = false;
                    if (response.isSuccessful()) {
                        mEventBus.post(Commons.LOCATION_UPDATE_FINISH);
                        Log.d("ACCOUNT CONTROLLER: ", "Location updated");
                    } else {
                        mEventBus.post(Commons.LOCATION_UPDATE_ERROR);
                        Log.d("ACCOUNT CONTROLLER: ", "Failed updating Location");
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    errorMessage = t.getMessage();
                    mBusy = false;
                    mEventBus.post(Commons.LOCATION_UPDATE_ERROR);
                    Log.d("ACCOUNT CONTROLLER: ", "Failed updating Location");
                }
            });
        }
    }

    public void sendFirebaseToken(String fcmToken) {
        if (!mBusy) {

            mBusy = true;
            mEventBus.post(Commons.FIREBASE_TOKEN_START);

            EndPointService apiService = EndPointService.getInstance(mContext);

            Call<Void> call = apiService.getEndPoint()
                    .updateFCMToken(new FCMTokenBody(fcmToken));
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {

                    mBusy = false;
                    if (response.isSuccessful()) {
                        mEventBus.post(Commons.FIREBASE_TOKEN_FINISH);
                        Log.d("SEND FCM CONTROLLER: ", "Token sent");
                    } else {
                        mEventBus.post(Commons.FIREBASE_TOKEN_ERROR);
                        Log.d("SEND FCM CONTROLLER: ", "ERROR");
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    mBusy = false;
                    mEventBus.post(Commons.FIREBASE_TOKEN_ERROR);
                    Log.d("SEND FCM CONTROLLER: ", "ERROR");
                }
            });
        }
    }

    public void askForHelp(double lat, double lng, int emergencyType, String address) {
        if (!mBusy) {
            mBusy = true;
            mEventBus.post(Commons.ASK_FOR_HELP_START);

            EndPointService apiService = EndPointService.getInstance(mContext);

            Call<AskForHelpResponse> call = apiService.getEndPoint().askForHelp(new AskForHelpBody(lat, lng, emergencyType, address));
            call.enqueue(new Callback<AskForHelpResponse>() {
                @Override
                public void onResponse(Call<AskForHelpResponse> call, Response<AskForHelpResponse> response) {
                    mBusy = false;
                    if (response.isSuccessful()) {
                        askForHelpResponse = response.body();
                        mEventBus.post(Commons.ASK_FOR_HELP_FINISH);
                        Log.d("ACCOUNT CONTROLLER: ", "askForHelp success");
                    } else {
                        mEventBus.post(Commons.ASK_FOR_HELP_ERROR);
                        Log.d("ACCOUNT CONTROLLER: ", "askForHelp Failed");
                    }
                }

                @Override
                public void onFailure(Call<AskForHelpResponse> call, Throwable t) {
                    errorMessage = t.getMessage();
                    mBusy = false;
                    mEventBus.post(Commons.ASK_FOR_HELP_ERROR);
                    Log.d("ACCOUNT CONTROLLER: ", "askForHelp Failed");
                }
            });
        }
    }

    public void setIsVolunteer(final boolean isOther, final boolean isFaint, final boolean isHeartAttack, final boolean isWound, final boolean isPanic, final int distance, final int availableStart, final int availableEnd) {
        int isVolunteer = 0;
        isVolunteer += (isOther) ? 1 : 0;
        isVolunteer += (isFaint) ? 2 : 0;
        isVolunteer += (isHeartAttack) ? 4 : 0;
        isVolunteer += (isWound) ? 8 : 0;
        isVolunteer += (isPanic) ? 16 : 0;
        if (!mBusy) {
            mBusy = true;
            mEventBus.post(Commons.IS_VOLUNTEER_START);

            EndPointService apiService = EndPointService.getInstance(mContext);

            Call<Void> call = apiService.getEndPoint().isVolunteer(new IsVolunteerBody(isVolunteer, distance, availableStart, availableEnd)); //distance is in meters
            final int finalIsVolunteer = isVolunteer;
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    mBusy = false;
                    if (response.isSuccessful()) {
                        mEventBus.post(Commons.IS_VOLUNTEER_FINISH);
                        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(mContext);
                        SharedPreferences.Editor edit = sp.edit();

                        edit.putBoolean(Commons.SHARED_PREFERENCE_IS_VOLUNTEER, finalIsVolunteer > 0);
                        edit.putInt(Commons.SHARED_PREFERENCE_IS_VOLUNTEER_FILTERS, finalIsVolunteer);
                        edit.putBoolean(Commons.SHARED_PREFERENCE_EMERGENCY_OTHER, isOther);
                        edit.putBoolean(Commons.SHARED_PREFERENCE_EMERGENCY_FAINT, isFaint);
                        edit.putBoolean(Commons.SHARED_PREFERENCE_EMERGENCY_HEART_ATTACK, isHeartAttack);
                        edit.putBoolean(Commons.SHARED_PREFERENCE_EMERGENCY_WOUND, isWound);
                        edit.putBoolean(Commons.SHARED_PREFERENCE_EMERGENCY_PANIC, isPanic);
                        edit.putInt(Commons.SHARED_PREFERENCE_EMERGENCY_RADIO, distance);
                        edit.putInt(Commons.SHARED_PREFERENCE_AVAILABLE_TIME_START, availableStart);
                        edit.putInt(Commons.SHARED_PREFERENCE_AVAILABLE_TIME_END, availableEnd);
                        edit.apply();

                        Log.d("ACCOUNT CONTROLLER: ", "setIsVolunteer success");
                    } else {
                        mEventBus.post(Commons.IS_VOLUNTEER_ERROR);
                        Log.d("ACCOUNT CONTROLLER: ", "setIsVolunteer Failed");
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    errorMessage = t.getMessage();
                    mBusy = false;
                    mEventBus.post(Commons.IS_VOLUNTEER_ERROR);
                    Log.d("ACCOUNT CONTROLLER: ", "setIsVolunteer Failed");
                }
            });
        }
    }

    ArrayList<EmergencyNotification> mEmergenciesList;

    public ArrayList<EmergencyNotification> getEmergenciesList() {
        return mEmergenciesList;
    }

    public void getEmergencies() {
        mBusy = false;
        if (!mBusy) {
            mBusy = true;
            mEventBus.post(Commons.GET_EMERGENCIES_START);

            EndPointService apiService = EndPointService.getInstance(mContext);

            Call<EmergenciesResponse> call = apiService.getEndPoint().getEmergencies();
            call.enqueue(new Callback<EmergenciesResponse>() {
                @Override
                public void onResponse(Call<EmergenciesResponse> call, Response<EmergenciesResponse> response) {
                    mBusy = false;
                    if (response.isSuccessful()) {
                        mEmergenciesList = response.body().getEmergencies();
                        mEventBus.post(Commons.GET_EMERGENCIES_FINISH);
                    } else {
                        mEventBus.post(Commons.GET_EMERGENCIES_ERROR);
                    }
                }

                @Override
                public void onFailure(Call<EmergenciesResponse> call, Throwable t) {
                    errorMessage = t.getMessage();
                    mBusy = false;
                    mEventBus.post(Commons.GET_EMERGENCIES_ERROR);
                }
            });
        }
    }

    public void modifyStatusEmergency(String email, int status) {
        if (!mBusy) {
            mBusy = true;
            mEventBus.post(Commons.MODIFY_EMERGENCY_START);

            EndPointService apiService = EndPointService.getInstance(mContext);

            Call<Void> call = apiService.getEndPoint().modifyStatusHelp(
                    new ModifyHelpBody(email, status)
            );
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    mBusy = false;
                    if (response.isSuccessful()) {
                        mEventBus.post(Commons.MODIFY_EMERGENCY_FINISH);
                    } else {
                        mEventBus.post(Commons.MODIFY_EMERGENCY_ERROR);
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    errorMessage = t.getMessage();
                    mBusy = false;
                    mEventBus.post(Commons.MODIFY_EMERGENCY_ERROR);
                }
            });
        }
    }

    public void getUser() {
        if (!mBusy) {
            mBusy = true;
            mEventBus.post(Commons.GET_USER_START);

            EndPointService apiService = EndPointService.getInstance(mContext);

            Call<User> call = apiService.getEndPoint().getUser();
            call.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    mBusy = false;
                    if (response.isSuccessful()) {
                        User user = response.body();
                        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(mContext);
                        SharedPreferences.Editor edit = sp.edit();
                        edit.putString(Commons.SHARED_PREFERENCE_EMAIL, user.getEmail());
                        edit.putString(Commons.SHARED_PREFERENCE_FIRST_NAME, user.getFirstName());
                        edit.putString(Commons.SHARED_PREFERENCE_LAST_NAME, user.getLastName());
                        edit.putString(Commons.SHARED_PREFERENCE_PHONE, user.getPhone());
                        edit.putString(Commons.SHARED_PREFERENCE_ADDRESS, user.getAddress());
                        edit.putInt(Commons.SHARED_PREFERENCE_AVAILABLE_TIME_START, user.getAvailableStart());
                        edit.putInt(Commons.SHARED_PREFERENCE_AVAILABLE_TIME_END, user.getAvailableEnd());

                        edit.putInt(Commons.SHARED_PREFERENCE_IS_VOLUNTEER_FILTERS, user.getIsVolunteer());
                        edit.putBoolean(Commons.SHARED_PREFERENCE_IS_VOLUNTEER, user.getIsVolunteer() > 0);
                        edit.putBoolean(Commons.SHARED_PREFERENCE_EMERGENCY_OTHER, (user.getIsVolunteer() & 0x0001) > 0);
                        edit.putBoolean(Commons.SHARED_PREFERENCE_EMERGENCY_FAINT, (user.getIsVolunteer() & 0x0002) > 0);
                        edit.putBoolean(Commons.SHARED_PREFERENCE_EMERGENCY_HEART_ATTACK, (user.getIsVolunteer() & 0x0004) > 0);
                        edit.putBoolean(Commons.SHARED_PREFERENCE_EMERGENCY_WOUND, (user.getIsVolunteer() & 0x0008) > 0);
                        edit.putInt(Commons.SHARED_PREFERENCE_EMERGENCY_RADIO, user.getRadio());
                        if(user.getEmergency() != null){
                            user.getEmergency().setDateTime(user.getEmergency().getDateTime() + 3600000 * 3);//corrimiento de 3hs entre la app y el server...
                            Gson gson = new Gson();
                            edit.putString(Commons.SHARED_PREFERENCE_EMERGENCY_CREATED, gson.toJson(user.getEmergency()));
                        }
                        edit.apply();
                        mEventBus.post(Commons.GET_USER_FINISH);
                    } else {
                        mEventBus.post(Commons.GET_USER_ERROR);
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    errorMessage = t.getMessage();
                    mBusy = false;
                    mEventBus.post(Commons.GET_USER_ERROR);
                }
            });
        }
    }

    public void updateUser(final String firstName, final String lastName, final String email, final String phone, final String address) {
        if (!mBusy) {
            mBusy = true;
            mEventBus.post(Commons.UPDATE_USER_START);

            EndPointService apiService = EndPointService.getInstance(mContext);

            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(mContext);
            Call<Void> call = apiService.getEndPoint().updateUser(
                    new User(
                            firstName,
                            lastName,
                            email,
                            phone,
                            address,
                            sp.getInt(Commons.SHARED_PREFERENCE_IS_VOLUNTEER_FILTERS, 0),
                            sp.getInt(Commons.SHARED_PREFERENCE_EMERGENCY_RADIO, 2000),
                            sp.getInt(Commons.SHARED_PREFERENCE_AVAILABLE_TIME_START, 0),
                            sp.getInt(Commons.SHARED_PREFERENCE_AVAILABLE_TIME_END, 0))
            );
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    mBusy = false;
                    if (response.isSuccessful()) {
                        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(mContext);
                        SharedPreferences.Editor edit = sp.edit();
                        edit.putString(Commons.SHARED_PREFERENCE_EMAIL, email);
                        edit.putString(Commons.SHARED_PREFERENCE_FIRST_NAME, firstName);
                        edit.putString(Commons.SHARED_PREFERENCE_LAST_NAME, lastName);
                        edit.putString(Commons.SHARED_PREFERENCE_PHONE, phone);
                        edit.putString(Commons.SHARED_PREFERENCE_ADDRESS, address);
                        edit.apply();
                        mEventBus.post(Commons.UPDATE_USER_FINISH);
                    } else {
                        mEventBus.post(Commons.UPDATE_USER_ERROR);
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    errorMessage = t.getMessage();
                    mBusy = false;
                    mEventBus.post(Commons.UPDATE_USER_ERROR);
                }
            });
        }
    }

    public void registerForEvents(Object subscriber) {
        mEventBus.register(subscriber);
    }

    public void unregisterForEvents(Object subscriber) {
        mEventBus.unregister(subscriber);
    }

    public AskForHelpResponse getAskForHelpResponse() {
        return askForHelpResponse;
    }
}
