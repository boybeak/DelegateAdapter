package com.github.boybeak.timepaper.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by gaoyunfei on 2017/9/4.
 */

public class Urls implements Parcelable {

    public String raw, full, regular, small, thumb, custom;

    protected Urls(Parcel in) {
        raw = in.readString();
        full = in.readString();
        regular = in.readString();
        small = in.readString();
        thumb = in.readString();
        custom = in.readString();
    }

    public static final Creator<Urls> CREATOR = new Creator<Urls>() {
        @Override
        public Urls createFromParcel(Parcel in) {
            return new Urls(in);
        }

        @Override
        public Urls[] newArray(int size) {
            return new Urls[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(raw);
        parcel.writeString(full);
        parcel.writeString(regular);
        parcel.writeString(small);
        parcel.writeString(thumb);
        parcel.writeString(custom);
    }
}
