package com.arie.onlineloan.models;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {
    private String userId;
    private String userPassword;
    private String userFullName;
    private String userPhone;
    private String userEmail;
    private String userAddress;

    public User() {

    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getUserFullName() {
        return userFullName;
    }

    public void setUserFullName(String userFullName) {
        this.userFullName = userFullName;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress;
    }

    public User(String userId, String userPassword, String userFullName, String userPhone, String userEmail, String userAddress) {
        this.userId = userId;
        this.userPassword = userPassword;
        this.userFullName = userFullName;
        this.userPhone = userPhone;
        this.userEmail = userEmail;
        this.userAddress = userAddress;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.userId);
        dest.writeString(this.userPassword);
        dest.writeString(this.userFullName);
        dest.writeString(this.userPhone);
        dest.writeString(this.userEmail);
        dest.writeString(this.userAddress);
    }

    protected User(Parcel in) {
        this.userId = in.readString();
        this.userPassword = in.readString();
        this.userFullName = in.readString();
        this.userPhone = in.readString();
        this.userEmail = in.readString();
        this.userAddress = in.readString();
    }

    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        @Override
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}