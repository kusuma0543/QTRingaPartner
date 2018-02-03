package com.ringaapp.ringapartner.db_javaclasses;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by kusuma on 10/27/2017.
 */

public class Categorieslist implements Parcelable {
    String rid;
    String service_categ_uid;
    String service_categ_name;
    String service_categ_fullimage;

    protected Categorieslist(Parcel in) {
        rid = in.readString();
        service_categ_uid = in.readString();
        service_categ_name = in.readString();
        service_categ_fullimage = in.readString();
    }

    public static final Creator<Categorieslist> CREATOR = new Creator<Categorieslist>() {
        @Override
        public Categorieslist createFromParcel(Parcel in) {
            return new Categorieslist(in);
        }

        @Override
        public Categorieslist[] newArray(int size) {
            return new Categorieslist[size];
        }
    };

    public String getRid() {
        return rid;
    }

    public void setRid(String rid) {
        this.rid = rid;
    }

    public String getService_categ_uid() {
        return service_categ_uid;
    }

    public void setService_categ_uid(String service_categ_uid) {
        this.service_categ_uid = service_categ_uid;
    }

    public String getService_categ_name() {
        return service_categ_name;
    }

    public void setService_categ_name(String service_categ_name) {
        this.service_categ_name = service_categ_name;
    }

    public String getService_categ_fullimage() {
        return service_categ_fullimage;
    }

    public void setService_categ_fullimage(String service_categ_fullimage) {
        this.service_categ_fullimage = service_categ_fullimage;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(rid);
        dest.writeString(service_categ_uid);
        dest.writeString(service_categ_name);
        dest.writeString(service_categ_fullimage);
    }
}
