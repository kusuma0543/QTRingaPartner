package com.ringaapp.ringapartner;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by kusuma on 11/17/2017.
 */

public class Imageret implements Parcelable{
    String partner_uid;
    String partner_images;

    protected Imageret(Parcel in) {
        partner_uid = in.readString();
        partner_images = in.readString();
    }

    public static final Creator<Imageret> CREATOR = new Creator<Imageret>() {
        @Override
        public Imageret createFromParcel(Parcel in) {
            return new Imageret(in);
        }

        @Override
        public Imageret[] newArray(int size) {
            return new Imageret[size];
        }
    };

    public String getPartner_uid() {
        return partner_uid;
    }

    public void setPartner_uid(String partner_uid) {
        this.partner_uid = partner_uid;
    }

    public String getPartner_images() {
        return partner_images;
    }

    public void setPartner_images(String partner_images) {
        this.partner_images = partner_images;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(partner_uid);
        dest.writeString(partner_images);
    }
}
