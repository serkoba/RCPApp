package com.bobygoma.redciudadana.backend;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by daniel.streitenberger on 21/04/2017.
 */
public class User implements Parcelable {
    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
    @SerializedName("firstName")
    String firstName;
    @SerializedName("lastName")
    String lastName;
    @SerializedName("email")
    String email;
    @SerializedName("phone")
    String phone;
    @SerializedName("address")
    String address;
    @SerializedName("isVolunteer")
    int isVolunteer = 0;
    @SerializedName("radio")
    int radio = 20;
    @SerializedName("avail_start")
    int availableStart;
    @SerializedName("avail_end")
    int availableEnd;
    @SerializedName("emergency")
    EmergencyNotification emergency;

    public User(String firstName, String lastName, String email, String phone, String address, int isVolunteer, int radio, int availableStart, int availableEnd) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.isVolunteer = isVolunteer;
        this.radio = radio;
        this.availableStart = availableStart;
        this.availableEnd = availableEnd;
    }

    public User() {
    }

    protected User(Parcel in) {
        this.firstName = in.readString();
        this.lastName = in.readString();
        this.email = in.readString();
        this.phone = in.readString();
        this.address = in.readString();
        this.isVolunteer = in.readInt();
        this.radio = in.readInt();
        this.availableStart = in.readInt();
        this.availableEnd = in.readInt();
        this.emergency = in.readParcelable(EmergencyNotification.class.getClassLoader());
    }

    public EmergencyNotification getEmergency() {
        return emergency;
    }

    public int getAvailableStart() {
        return availableStart;
    }

    public int getAvailableEnd() {
        return availableEnd;
    }

    public int getIsVolunteer() {
        return isVolunteer;
    }

    public int getRadio() {
        return radio;
    }

    public String getFirstName() {
        return (firstName != null) ? firstName : "";
    }

    public String getLastName() {
        return (lastName != null) ? lastName : "";
    }

    public String getEmail() {
        return (email != null) ? email : "";
    }

    public String getPhone() {
        return (phone != null) ? phone : "";
    }

    public String getAddress() {
        return (address != null) ? address : "";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.firstName);
        dest.writeString(this.lastName);
        dest.writeString(this.email);
        dest.writeString(this.phone);
        dest.writeString(this.address);
        dest.writeInt(this.isVolunteer);
        dest.writeInt(this.radio);
        dest.writeInt(this.availableStart);
        dest.writeInt(this.availableEnd);
        dest.writeParcelable(this.emergency, flags);
    }
}
