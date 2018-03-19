package com.tenz.hotchpotch.module.home.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Author: TenzLiu
 * Date: 2018-03-16 15:45
 * Description: 联系人
 */

public class Contacts implements Parcelable {

    private String name;
    private List<String> phone;
    private String email;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getPhone() {
        return phone;
    }

    public void setPhone(List<String> phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "Contacts{" +
                "name='" + name + '\'' +
                ", phone=" + phone +
                ", email='" + email + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeStringList(this.phone);
        dest.writeString(this.email);
    }

    public Contacts() {
    }

    protected Contacts(Parcel in) {
        this.name = in.readString();
        this.phone = in.createStringArrayList();
        this.email = in.readString();
    }

    public static final Creator<Contacts> CREATOR = new Creator<Contacts>() {
        @Override
        public Contacts createFromParcel(Parcel source) {
            return new Contacts(source);
        }

        @Override
        public Contacts[] newArray(int size) {
            return new Contacts[size];
        }
    };

}
