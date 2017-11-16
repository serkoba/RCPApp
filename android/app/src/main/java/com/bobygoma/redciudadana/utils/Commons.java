package com.bobygoma.redciudadana.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.bobygoma.redciudadana.R;
import com.bobygoma.redciudadana.backend.EmergencyList;
import com.bobygoma.redciudadana.backend.EmergencyNotification;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by daniel.streitenberger on 27/02/2017.
 */
public class Commons {

    public static final String SHARED_PREFERENCE_TOKEN = "SHARED_PREFERENCE_TOKEN";
    public static final String SHARED_PREFERENCE_EMAIL = "SHARED_PREFERENCE_EMAIL";
    public static final String SHARED_PREFERENCE_FIRST_NAME = "SHARED_PREFERENCE_FIRST_NAME";
    public static final String SHARED_PREFERENCE_LAST_NAME = "SHARED_PREFERENCE_LAST_NAME";
    public static final String SHARED_PREFERENCE_PHONE = "SHARED_PREFERENCE_PHONE";
    public static final String SHARED_PREFERENCE_ADDRESS = "SHARED_PREFERENCE_ADDRESS";
    public static final String SHARED_PREFERENCE_AVAILABLE_TIME_START = "SHARED_PREFERENCE_AVAILABLE_TIME_START";
    public static final String SHARED_PREFERENCE_AVAILABLE_TIME_END = "SHARED_PREFERENCE_AVAILABLE_TIME_END";
    //Filters
    public static final String SHARED_PREFERENCE_IS_VOLUNTEER = "SHARED_PREFERENCE_IS_VOLUNTEER";
    public static final String SHARED_PREFERENCE_IS_VOLUNTEER_FILTERS = "SHARED_PREFERENCE_IS_VOLUNTEER_FILTERS";
    public static final String SHARED_PREFERENCE_EMERGENCY_OTHER = "SHARED_PREFERENCE_EMERGENCY_OTHER";
    public static final String SHARED_PREFERENCE_EMERGENCY_HEART_ATTACK = "SHARED_PREFERENCE_EMERGENCY_HEART_ATTACK";
    public static final String SHARED_PREFERENCE_EMERGENCY_PANIC = "SHARED_PREFERENCE_EMERGENCY_PANIC";
    public static final String SHARED_PREFERENCE_EMERGENCY_WOUND = "SHARED_PREFERENCE_EMERGENCY_WOUND";
    public static final String SHARED_PREFERENCE_EMERGENCY_FAINT = "SHARED_PREFERENCE_EMERGENCY_FAINT";
    public static final String SHARED_PREFERENCE_EMERGENCY_RADIO = "SHARED_PREFERENCE_EMERGENCY_RADIO";

    public static final String SHARED_PREFERENCE_EMERGENCY_CREATED = "SHARED_PREFERENCE_EMERGENCY_CREATED";

    public static final int SIGN_UP_START = 0;
    public static final int SIGN_UP_FINISH = 1;
    public static final int SIGN_UP_ERROR = 2;

    public static final int LOGIN_START = 3;
    public static final int LOGIN_FINISH = 4;
    public static final int LOGIN_ERROR = 5;

    public static final int LOGOUT_START = 6;
    public static final int LOGOUT_FINISH = 7;
    public static final int LOGOUT_ERROR = 8;

    public static final int FIREBASE_TOKEN_START = 9;
    public static final int FIREBASE_TOKEN_FINISH = 10;
    public static final int FIREBASE_TOKEN_ERROR = 11;

    public static final int LOCATION_UPDATE_START = 12;
    public static final int LOCATION_UPDATE_FINISH = 13;
    public static final int LOCATION_UPDATE_ERROR = 14;

    public static final int ASK_FOR_HELP_START = 15;
    public static final int ASK_FOR_HELP_FINISH = 16;
    public static final int ASK_FOR_HELP_ERROR = 17;

    public static final int IS_VOLUNTEER_START = 18;
    public static final int IS_VOLUNTEER_FINISH = 19;
    public static final int IS_VOLUNTEER_ERROR = 20;

    public static final int GET_EMERGENCIES_START = 21;
    public static final int GET_EMERGENCIES_FINISH = 22;
    public static final int GET_EMERGENCIES_ERROR = 23;

    public static final int MODIFY_EMERGENCY_START = 24;
    public static final int MODIFY_EMERGENCY_FINISH = 25;
    public static final int MODIFY_EMERGENCY_ERROR = 26;

    public static final int UPDATE_USER_START = 27;
    public static final int UPDATE_USER_FINISH = 28;
    public static final int UPDATE_USER_ERROR = 29;

    public static final int GET_USER_START = 30;
    public static final int GET_USER_FINISH = 31;
    public static final int GET_USER_ERROR = 32;


    public static final double BAHIA_BLANCA_LAT = -38.7183403;
    public static final double BAHIA_BLANCA_LNG = -62.2663268;
    public static final float DEFAULT_ZOOM = 13;

    public static final int EMERGENCY_OTHER = 1;
    public static final int EMERGENCY_FAINT = 2;
    public static final int EMERGENCY_HEART_ATTACK = 4;
    public static final int EMERGENCY_WOUND = 8;
    public static final int EMERGENCY_PANIC = 16;

    public static String getEmergencyTypeString(int emergencyType, Context context) {
        String emergencyString = "";
        switch (emergencyType) {
            case EMERGENCY_OTHER: {
                emergencyString = context.getString(R.string.other);
                break;
            }
            case EMERGENCY_FAINT: {
                emergencyString = context.getString(R.string.fainting);
                break;
            }
            case EMERGENCY_HEART_ATTACK: {
                emergencyString = context.getString(R.string.heart_attack);
                break;
            }
            case EMERGENCY_WOUND: {
                emergencyString = context.getString(R.string.injured_accident);
                break;
            }
            case EMERGENCY_PANIC: {
                emergencyString = context.getString(R.string.panico);
                break;
            }
            default:{
                emergencyString = context.getString(R.string.unknown);
                break;
            }
        }
        return emergencyString;
    }
    public static boolean isEmergencyActive(long timeInMillis) {

        Calendar now = Calendar.getInstance();
        Calendar cdate = Calendar.getInstance();
        cdate.setTimeInMillis(timeInMillis);
        long result = ((now.getTimeInMillis()) - (cdate.getTimeInMillis()));

        return result < 3600000 ; //1 hour
    }
}
