package com.github.boybeak.timepaper.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by gaoyunfei on 2017/9/4.
 */

public class Links implements Parcelable {

    public String self, html, photos, likes, portfolio, download, download_location;


    protected Links(Parcel in) {
        self = in.readString();
        html = in.readString();
        photos = in.readString();
        likes = in.readString();
        portfolio = in.readString();
        download = in.readString();
        download_location = in.readString();
    }

    public static final Creator<Links> CREATOR = new Creator<Links>() {
        @Override
        public Links createFromParcel(Parcel in) {
            return new Links(in);
        }

        @Override
        public Links[] newArray(int size) {
            return new Links[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(self);
        parcel.writeString(html);
        parcel.writeString(photos);
        parcel.writeString(likes);
        parcel.writeString(portfolio);
        parcel.writeString(download);
        parcel.writeString(download_location);
    }
}
