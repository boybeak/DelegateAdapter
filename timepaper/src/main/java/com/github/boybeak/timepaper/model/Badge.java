package com.github.boybeak.timepaper.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by gaoyunfei on 2017/9/9.
 */

public class Badge implements Parcelable {

    public String title, slug, link;
    public boolean primary;

    protected Badge(Parcel in) {
        title = in.readString();
        slug = in.readString();
        link = in.readString();
        primary = in.readByte() != 0;
    }

    public static final Creator<Badge> CREATOR = new Creator<Badge>() {
        @Override
        public Badge createFromParcel(Parcel in) {
            return new Badge(in);
        }

        @Override
        public Badge[] newArray(int size) {
            return new Badge[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(title);
        parcel.writeString(slug);
        parcel.writeString(link);
        parcel.writeByte((byte) (primary ? 1 : 0));
    }
}
