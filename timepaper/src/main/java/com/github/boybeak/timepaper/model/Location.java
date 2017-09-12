package com.github.boybeak.timepaper.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by gaoyunfei on 2017/9/6.
 */

public class Location implements Parcelable {

    public String title, name, city, country;
    public Position position;


    protected Location(Parcel in) {
        title = in.readString();
        name = in.readString();
        city = in.readString();
        country = in.readString();
        position = in.readParcelable(Position.class.getClassLoader());
    }

    public static final Creator<Location> CREATOR = new Creator<Location>() {
        @Override
        public Location createFromParcel(Parcel in) {
            return new Location(in);
        }

        @Override
        public Location[] newArray(int size) {
            return new Location[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(title);
        parcel.writeString(name);
        parcel.writeString(city);
        parcel.writeString(country);
        parcel.writeParcelable(position, i);
    }
}
