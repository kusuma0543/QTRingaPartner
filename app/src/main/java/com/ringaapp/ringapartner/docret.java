package com.ringaapp.ringapartner;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by kusuma on 12/21/2017.
 */

public class docret implements Parcelable{
    String partner_uid;
    String partner_documents;
    String partner_documentname;

    protected docret(Parcel in) {
        partner_uid = in.readString();
        partner_documents = in.readString();
        partner_documentname = in.readString();
    }

    public static final Creator<docret> CREATOR = new Creator<docret>() {
        @Override
        public docret createFromParcel(Parcel in) {
            return new docret(in);
        }

        @Override
        public docret[] newArray(int size) {
            return new docret[size];
        }
    };

    public String getPartner_uid() {
        return partner_uid;
    }

    public void setPartner_uid(String partner_uid) {
        this.partner_uid = partner_uid;
    }

    public String getPartner_documents() {
        return partner_documents;
    }

    public void setPartner_documents(String partner_documents) {
        this.partner_documents = partner_documents;
    }

    public String getPartner_documentname() {
        return partner_documentname;
    }

    public void setPartner_documentname(String partner_documentname) {
        this.partner_documentname = partner_documentname;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(partner_uid);
        dest.writeString(partner_documents);
        dest.writeString(partner_documentname);
    }
}
