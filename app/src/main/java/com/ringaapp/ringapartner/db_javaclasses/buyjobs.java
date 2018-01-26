package com.ringaapp.ringapartner.db_javaclasses;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by andriod on 22/1/18.
 */

public class buyjobs implements Parcelable{
     String rid;
     String jobs_value;
     String  jobs_count;

    protected buyjobs(Parcel in) {
        rid = in.readString();
        jobs_value = in.readString();
        jobs_count = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(rid);
        dest.writeString(jobs_value);
        dest.writeString(jobs_count);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<buyjobs> CREATOR = new Creator<buyjobs>() {
        @Override
        public buyjobs createFromParcel(Parcel in) {
            return new buyjobs(in);
        }

        @Override
        public buyjobs[] newArray(int size) {
            return new buyjobs[size];
        }
    };

    public String getRid() {
        return rid;
    }

    public void setRid(String rid) {
        this.rid = rid;
    }

    public String getJobs_value() {
        return jobs_value;
    }

    public void setJobs_value(String jobs_value) {
        this.jobs_value = jobs_value;
    }

    public String getJobs_count() {
        return jobs_count;
    }

    public void setJobs_count(String jobs_count) {
        this.jobs_count = jobs_count;
    }
}
