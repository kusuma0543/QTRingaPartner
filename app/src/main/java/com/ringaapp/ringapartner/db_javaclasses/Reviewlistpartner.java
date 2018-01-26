package com.ringaapp.ringapartner.db_javaclasses;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by veena on 1/21/18.
 */

public class Reviewlistpartner implements Parcelable {

    String user_profile_image;
    String user_name;
    String user_address_cityname;
    String user_feedback;

    public Reviewlistpartner(String user_profile_image, String user_name, String user_address_cityname, String user_feedback) {
        this.user_profile_image = user_profile_image;
        this.user_name = user_name;
        this.user_address_cityname = user_address_cityname;
        this.user_feedback = user_feedback;
    }

    public String getUser_profile_image() {
        return user_profile_image;
    }

    public void setUser_profile_image(String user_profile_image) {
        this.user_profile_image = user_profile_image;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_address_cityname() {
        return user_address_cityname;
    }

    public void setUser_address_cityname(String user_address_cityname) {
        this.user_address_cityname = user_address_cityname;
    }

    public String getUser_feedback() {
        return user_feedback;
    }

    public void setUser_feedback(String user_feedback) {
        this.user_feedback = user_feedback;
    }

    public static Creator<Reviewlistpartner> getCREATOR() {
        return CREATOR;
    }

    protected Reviewlistpartner(Parcel in) {
        user_profile_image = in.readString();
        user_name = in.readString();
        user_address_cityname = in.readString();
        user_feedback = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(user_profile_image);
        dest.writeString(user_name);
        dest.writeString(user_address_cityname);
        dest.writeString(user_feedback);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Reviewlistpartner> CREATOR = new Creator<Reviewlistpartner>() {
        @Override
        public Reviewlistpartner createFromParcel(Parcel in) {
            return new Reviewlistpartner(in);
        }

        @Override
        public Reviewlistpartner[] newArray(int size) {
            return new Reviewlistpartner[size];
        }
    };
}
