package com.ringaapp.ringapartner.db_javaclasses;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by andriod on 21/1/18.
 */

public class ProofImageRet implements Parcelable{
    String partner_uid;
    String proof_images;

    protected ProofImageRet(Parcel in) {
        partner_uid = in.readString();
        proof_images = in.readString();
        row_count = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(partner_uid);
        dest.writeString(proof_images);
        dest.writeString(row_count);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ProofImageRet> CREATOR = new Creator<ProofImageRet>() {
        @Override
        public ProofImageRet createFromParcel(Parcel in) {
            return new ProofImageRet(in);
        }

        @Override
        public ProofImageRet[] newArray(int size) {
            return new ProofImageRet[size];
        }
    };

    public String getPartner_uid() {
        return partner_uid;
    }

    public void setPartner_uid(String partner_uid) {
        this.partner_uid = partner_uid;
    }

    public String getProof_images() {
        return proof_images;
    }

    public void setProof_images(String proof_images) {
        this.proof_images = proof_images;
    }

    public String getRow_count() {
        return row_count;
    }

    public void setRow_count(String row_count) {
        this.row_count = row_count;
    }

    String row_count;

}

