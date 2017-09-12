package com.github.boybeak.timepaper.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by gaoyunfei on 2017/9/6.
 */

public class CurrentUserCollection implements Parcelable {

    public int id;
    public String title;
    public boolean curated;
    public Photo cover_photo;
    public User user;
    public Links links;

    protected CurrentUserCollection(Parcel in) {
        id = in.readInt();
        title = in.readString();
        curated = in.readByte() != 0;
        user = in.readParcelable(User.class.getClassLoader());
        links = in.readParcelable(Links.class.getClassLoader());
    }

    public static final Creator<CurrentUserCollection> CREATOR = new Creator<CurrentUserCollection>() {
        @Override
        public CurrentUserCollection createFromParcel(Parcel in) {
            return new CurrentUserCollection(in);
        }

        @Override
        public CurrentUserCollection[] newArray(int size) {
            return new CurrentUserCollection[size];
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
        parcel.writeByte((byte) (curated ? 1 : 0));
        parcel.writeParcelable(user, i);
        parcel.writeParcelable(links, i);
    }
}
