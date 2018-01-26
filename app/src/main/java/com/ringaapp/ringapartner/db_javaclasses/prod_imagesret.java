package com.ringaapp.ringapartner.db_javaclasses;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by andriod on 25/1/18.
 */

public class prod_imagesret implements Parcelable{
    String partner_uid;
    String partner_product_images;
    String row_count;

    protected prod_imagesret(Parcel in) {
        partner_uid = in.readString();
        partner_product_images = in.readString();
        row_count = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(partner_uid);
        dest.writeString(partner_product_images);
        dest.writeString(row_count);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<prod_imagesret> CREATOR = new Creator<prod_imagesret>() {
        @Override
        public prod_imagesret createFromParcel(Parcel in) {
            return new prod_imagesret(in);
        }

        @Override
        public prod_imagesret[] newArray(int size) {
            return new prod_imagesret[size];
        }
    };

    public String getPartner_uid() {
        return partner_uid;
    }

    public void setPartner_uid(String partner_uid) {
        this.partner_uid = partner_uid;
    }

    public String getPartner_product_images() {
        return partner_product_images;
    }

    public void setPartner_product_images(String partner_product_images) {
        this.partner_product_images = partner_product_images;
    }

    public String getRow_count() {
        return row_count;
    }

    public void setRow_count(String row_count) {
        this.row_count = row_count;
    }
}
