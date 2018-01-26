package com.ringaapp.ringapartner.db_javaclasses;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by kusuma on 12/8/2017.
 */

public class Bannerlist implements Parcelable{
    String partner_uid;
    String partner_images;

    protected Bannerlist(Parcel in) {
        partner_uid = in.readString();
        partner_images = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(partner_uid);
        dest.writeString(partner_images);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Bannerlist> CREATOR = new Creator<Bannerlist>() {
        @Override
        public Bannerlist createFromParcel(Parcel in) {
            return new Bannerlist(in);
        }

        @Override
        public Bannerlist[] newArray(int size) {
            return new Bannerlist[size];
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
}
