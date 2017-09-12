package com.github.boybeak.timepaper.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by gaoyunfei on 2017/9/6.
 */

public class Category implements Parcelable {

    public int id;
    public String title;
    public int photo_count;

    public Links links;

    protected Category(Parcel in) {
        id = in.readInt();
        title = in.readString();
        photo_count = in.readInt();
        links = in.readParcelable(Links.class.getClassLoader());
    }

    public static final Creator<Category> CREATOR = new Creator<Category>() {
        @Override
        public Category createFromParcel(Parcel in) {
            return new Category(in);
        }

        @Override
        public Category[] newArray(int size) {
            return new Category[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(title);
        parcel.writeInt(photo_count);
        parcel.writeParcelable(links, i);
    }
}
