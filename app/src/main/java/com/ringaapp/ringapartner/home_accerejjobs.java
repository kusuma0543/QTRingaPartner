package com.ringaapp.ringapartner;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by kusuma on 12/28/2017.
 */

public class home_accerejjobs implements Parcelable{
    String booking_uid;
    String user_uid;
    String partner_uid;
    String user_name;
    String service_subcateg_name;
    String service_booking_description;
    String 	service_booking_address;
    String user_email;
    String user_mobile_number;
String service_categ_name;
String user_address_cityname;

    protected home_accerejjobs(Parcel in) {
        booking_uid = in.readString();
        user_uid = in.readString();
        partner_uid = in.readString();
        user_name = in.readString();
        service_subcateg_name = in.readString();
        service_booking_description = in.readString();
        service_booking_address = in.readString();
        user_email = in.readString();
        user_mobile_number = in.readString();
        service_categ_name = in.readString();
        user_address_cityname = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(booking_uid);
        dest.writeString(user_uid);
        dest.writeString(partner_uid);
        dest.writeString(user_name);
        dest.writeString(service_subcateg_name);
        dest.writeString(service_booking_description);
        dest.writeString(service_booking_address);
        dest.writeString(user_email);
        dest.writeString(user_mobile_number);
        dest.writeString(service_categ_name);
        dest.writeString(user_address_cityname);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<home_accerejjobs> CREATOR = new Creator<home_accerejjobs>() {
        @Override
        public home_accerejjobs createFromParcel(Parcel in) {
            return new home_accerejjobs(in);
        }

        @Override
        public home_accerejjobs[] newArray(int size) {
            return new home_accerejjobs[size];
        }
    };

    public String getBooking_uid() {
        return booking_uid;
    }

    public void setBooking_uid(String booking_uid) {
        this.booking_uid = booking_uid;
    }

    public String getUser_uid() {
        return user_uid;
    }

    public void setUser_uid(String user_uid) {
        this.user_uid = user_uid;
    }

    public String getPartner_uid() {
        return partner_uid;
    }

    public void setPartner_uid(String partner_uid) {
        this.partner_uid = partner_uid;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getService_subcateg_name() {
        return service_subcateg_name;
    }

    public void setService_subcateg_name(String service_subcateg_name) {
        this.service_subcateg_name = service_subcateg_name;
    }

    public String getService_booking_description() {
        return service_booking_description;
    }

    public void setService_booking_description(String service_booking_description) {
        this.service_booking_description = service_booking_description;
    }

    public String getService_booking_address() {
        return service_booking_address;
    }

    public void setService_booking_address(String service_booking_address) {
        this.service_booking_address = service_booking_address;
    }

    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    public String getUser_mobile_number() {
        return user_mobile_number;
    }

    public void setUser_mobile_number(String user_mobile_number) {
        this.user_mobile_number = user_mobile_number;
    }

    public String getService_categ_name() {
        return service_categ_name;
    }

    public void setService_categ_name(String service_categ_name) {
        this.service_categ_name = service_categ_name;
    }

    public String getUser_address_cityname() {
        return user_address_cityname;
    }

    public void setUser_address_cityname(String user_address_cityname) {
        this.user_address_cityname = user_address_cityname;
    }
}
