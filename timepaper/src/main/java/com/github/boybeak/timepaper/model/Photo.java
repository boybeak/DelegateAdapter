package com.github.boybeak.timepaper.model;

import android.graphics.Color;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by gaoyunfei on 2017/9/4.
 */

public class Photo implements Parcelable {

    public String id;
//    public Date created_at, updated_at;
    public int width, height;
    public String color;
    public int downloads;
    public int likes;
    public int views;
    public boolean liked_by_user;
    public String description;
    public Exif exif;
    public Location location;
    public Urls urls;
    public Links links;
    public User user;

    private int bgColor;

    public int getBgColor () {
        try {
            return Color.parseColor(color);
        } catch (Exception e) {
            return Color.LTGRAY;
        }
    }

    protected Photo(Parcel in) {
        id = in.readString();
        width = in.readInt();
        height = in.readInt();
        color = in.readString();
        downloads = in.readInt();
        likes = in.readInt();
        views = in.readInt();
        liked_by_user = in.readByte() != 0;
        description = in.readString();
        exif = in.readParcelable(Exif.class.getClassLoader());
        location = in.readParcelable(Location.class.getClassLoader());
        urls = in.readParcelable(Urls.class.getClassLoader());
        links = in.readParcelable(Links.class.getClassLoader());
        user = in.readParcelable(User.class.getClassLoader());
        bgColor = in.readInt();
    }

    public static final Creator<Photo> CREATOR = new Creator<Photo>() {
        @Override
        public Photo createFromParcel(Parcel in) {
            return new Photo(in);
        }

        @Override
        public Photo[] newArray(int size) {
            return new Photo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeInt(width);
        parcel.writeInt(height);
        parcel.writeString(color);
        parcel.writeInt(downloads);
        parcel.writeInt(likes);
        parcel.writeInt(views);
        parcel.writeByte((byte) (liked_by_user ? 1 : 0));
        parcel.writeString(description);
        parcel.writeParcelable(exif, i);
        parcel.writeParcelable(location, i);
        parcel.writeParcelable(urls, i);
        parcel.writeParcelable(links, i);
        parcel.writeParcelable(user, i);
        parcel.writeInt(bgColor);
    }
}
