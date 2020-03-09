package com.arie.onlineloan.models;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {
    private String userId;
    private String password;
    private String fullname;
    private String nik;
    private String phone;
    private String email;
    private String address;
    private String role;

    public User() {

    }


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getNik() {
        return nik;
    }

    public void setNik(String nik) {
        this.nik = nik;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public User(String userId, String password, String fullname, String nik, String phone, String email, String address, String role) {
        this.userId = userId;
        this.password = password;
        this.fullname = fullname;
        this.nik = nik;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.role = role;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.userId);
        dest.writeString(this.password);
        dest.writeString(this.fullname);
        dest.writeString(this.nik);
        dest.writeString(this.phone);
        dest.writeString(this.email);
        dest.writeString(this.address);
        dest.writeString(this.role);
    }

    protected User(Parcel in) {
        this.userId = in.readString();
        this.password = in.readString();
        this.fullname = in.readString();
        this.nik = in.readString();
        this.phone = in.readString();
        this.email = in.readString();
        this.address = in.readString();
        this.role = in.readString();
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