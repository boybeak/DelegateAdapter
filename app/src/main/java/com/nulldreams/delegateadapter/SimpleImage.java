package com.nulldreams.delegateadapter;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by gaoyunfei on 2017/8/30.
 */

public class SimpleImage implements Parcelable {

    private int id;
    public String name;
    public int width, height;
    public String data;

    public SimpleImage(int id, String name, int width, int height, String data) {
        this.id = id;
        this.name = name;
        this.width = width;
        this.height = height;
        this.data = data;
    }

    protected SimpleImage(Parcel in) {
        id = in.readInt();
        name = in.readString();
        width = in.readInt();
        height = in.readInt();
        data = in.readString();
    }

    public static final Creator<SimpleImage> CREATOR = new Creator<SimpleImage>() {
        @Override
        public SimpleImage createFromParcel(Parcel in) {
            return new SimpleImage(in);
        }

        @Override
        public SimpleImage[] newArray(int size) {
            return new SimpleImage[size];
        }
    };

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public String getData() {
        return data;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(name);
        parcel.writeInt(width);
        parcel.writeInt(height);
        parcel.writeString(data);
    }
}
