package com.android.test.model;

import android.net.Uri;
import android.os.Parcel;

/**
 * Created by Nitish on 24/2/17.
 */

public class DataModel extends AppBaseModel{

    Uri imagePath;
    String userName = "";
    String email = "";
    String phoneNumber = "";
    String dob = "";
    String gender = "";


    public DataModel() {
    }

    public DataModel(Parcel in) {
        this.imagePath = Uri.parse(in.readString());
        this.userName = in.readString();
        this.email = in.readString();
        this.phoneNumber = in.readString();
        this.dob = in.readString();
        this.gender = in.readString();
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(imagePath.toString());
        parcel.writeString(userName);
        parcel.writeString(email);
        parcel.writeString(phoneNumber);
        parcel.writeString(dob);
        parcel.writeString(gender);
    }

    public Uri getImagePath() {
        return imagePath;
    }

    public void setImagePath(Uri imagePath) {
        this.imagePath = imagePath;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public static final Creator<DataModel> CREATOR = new Creator<DataModel>() {
        @Override
        public DataModel createFromParcel(Parcel in) {
            return new DataModel(in);
        }

        @Override
        public DataModel[] newArray(int size) {
            return new DataModel[size];
        }
    };


}
