package com.github.boybeak.timepaper.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Position implements Parcelable {

    public float latitude, longitude;

    protected Position(Parcel in) {
        latitude = in.readFloat();
        longitude = in.readFloat();
    }

    public static final Creator<Position> CREATOR = new Creator<Position>() {
        @Override
        public Position createFromParcel(Parcel in) {
            return new Position(in);
        }

        @Override
        public Position[] newArray(int size) {
            return new Position[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeFloat(latitude);
        parcel.writeFloat(longitude);
    }
}