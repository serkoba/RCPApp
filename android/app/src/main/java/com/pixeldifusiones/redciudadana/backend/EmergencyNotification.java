package com.pixeldifusiones.redciudadana.backend;

import android.os.Parcel;
import android.os.Parcelable;

import com.pixeldifusiones.redciudadana.utils.Commons;
import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

/**
 * Created by daniel.streitenberger on 22/03/2017.
 */
public class EmergencyNotification implements Parcelable {

    @SerializedName("id")
    int id;
    @SerializedName("latitude")
    double latitude;
    @SerializedName("longitude")
    double longitude;
    @SerializedName("emergencyType")
    int emergencyType;
    @SerializedName("dateTime")
    long dateTime;
    @SerializedName("email")
    String email;
    @SerializedName("firstName")
    String firstName;
    @SerializedName("lastName")
    String lastName;
    @SerializedName("address")
    String address;

    @SerializedName("usersNotified")
    int usersNotified;

    public String getAddress() {
        return address;
    }

    public EmergencyNotification(double latitude, double longitude, int emergencyType, long dateTime, String email, String firstName, String lastName, String address, int usersNotified) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.emergencyType = emergencyType;
        this.dateTime = dateTime;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.usersNotified = usersNotified;
    }

    public EmergencyNotification(String emergencyNotification) {
        try {
            JSONObject emergencyObject = new JSONObject(emergencyNotification);
            latitude = emergencyObject.has("latitude") ? emergencyObject.getDouble("latitude") : Commons.BAHIA_BLANCA_LAT;
            longitude = emergencyObject.has("longitude") ? emergencyObject.getDouble("longitude") : Commons.BAHIA_BLANCA_LNG;
            emergencyType = emergencyObject.has("emergency") ? emergencyObject.getInt("emergency") : 0;
            dateTime = emergencyObject.has("dateTime") ? emergencyObject.getLong("dateTime") : Calendar.getInstance().getTimeInMillis();
            email = emergencyObject.has("email") ? emergencyObject.getString("email") : "";
            address = emergencyObject.has("address") ? emergencyObject.getString("address") : "";
            firstName = emergencyObject.has("firstName") ? emergencyObject.getString("firstName") : "";
            if (firstName == null) {
                firstName = "";
            }
            lastName = emergencyObject.has("lastName") ? emergencyObject.getString("lastName") : "";
            if (lastName == null) {
                lastName = "";
            }
            usersNotified = emergencyObject.has("usersNotified") ? emergencyObject.getInt("usersNotified") : 0;
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void setDateTime(long dateTime) {
        this.dateTime = dateTime;
    }

    public int getUsersNotified() {
        return usersNotified;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public int getEmergencyType() {
        return emergencyType;
    }

    public long getDateTime() {
        return dateTime;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public int getId() {
        return id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeDouble(this.latitude);
        dest.writeDouble(this.longitude);
        dest.writeInt(this.emergencyType);
        dest.writeLong(this.dateTime);
        dest.writeString(this.email);
        dest.writeString(this.firstName);
        dest.writeString(this.lastName);
        dest.writeString(this.address);
        dest.writeInt(this.usersNotified);
    }

    protected EmergencyNotification(Parcel in) {
        this.id = in.readInt();
        this.latitude = in.readDouble();
        this.longitude = in.readDouble();
        this.emergencyType = in.readInt();
        this.dateTime = in.readLong();
        this.email = in.readString();
        this.firstName = in.readString();
        this.lastName = in.readString();
        this.address = in.readString();
        this.usersNotified = in.readInt();
    }

    public static final Creator<EmergencyNotification> CREATOR = new Creator<EmergencyNotification>() {
        @Override
        public EmergencyNotification createFromParcel(Parcel source) {
            return new EmergencyNotification(source);
        }

        @Override
        public EmergencyNotification[] newArray(int size) {
            return new EmergencyNotification[size];
        }
    };
}
