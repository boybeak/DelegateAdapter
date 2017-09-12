package com.github.boybeak.timepaper.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

/**
 * Created by gaoyunfei on 2017/9/4.
 */

public class Exif implements Parcelable {

    public String make, model, exposure_time, aperture, focal_length;
    public int iso;

    public boolean isValid () {
        return !TextUtils.isEmpty(make) && !TextUtils.isEmpty(model)
                && !TextUtils.isEmpty(exposure_time) && !TextUtils.isEmpty(aperture)
                && !TextUtils.isEmpty(focal_length) && iso != 0;
    }

    protected Exif(Parcel in) {
        make = in.readString();
        model = in.readString();
        exposure_time = in.readString();
        aperture = in.readString();
        focal_length = in.readString();
        iso = in.readInt();
    }

    public static final Creator<Exif> CREATOR = new Creator<Exif>() {
        @Override
        public Exif createFromParcel(Parcel in) {
            return new Exif(in);
        }

        @Override
        public Exif[] newArray(int size) {
            return new Exif[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(make);
        parcel.writeString(model);
        parcel.writeString(exposure_time);
        parcel.writeString(aperture);
        parcel.writeString(focal_length);
        parcel.writeInt(iso);
    }
}
