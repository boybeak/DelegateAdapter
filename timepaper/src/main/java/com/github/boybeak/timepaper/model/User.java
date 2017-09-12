package com.github.boybeak.timepaper.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

/**
 * Created by gaoyunfei on 2017/9/4.
 * {
 "id": "pXhwzz1JtQU",
 "updated_at": "2016-07-10T11:00:01-05:00",
 "username": "jimmyexample",
 "first_name": "James",
 "last_name": "Example",
 "twitter_username": "jimmy",
 "portfolio_url": null,
 "bio": "The user's bio",
 "location": "Montreal, Qc",
 "total_likes": 20,
 "total_photos": 10,
 "total_collections": 5,
 "followed_by_user": false,
 "downloads": 4321,
 "uploads_remaining": 4,
 "instagram_username": "james-example",
 "location": null,
 "email": "jim@example.com",
 "links": {
 "self": "https://api.unsplash.com/users/jimmyexample",
 "html": "https://unsplash.com/jimmyexample",
 "photos": "https://api.unsplash.com/users/jimmyexample/photos",
 "likes": "https://api.unsplash.com/users/jimmyexample/likes",
 "portfolio": "https://api.unsplash.com/users/jimmyexample/portfolio"
 }
 }
 */

public class User implements Parcelable {

    public String id, username, first_name, last_name,
            twitter_username, portfolio_url, bio, location,
            instagram_username, email;
    public int total_likes, total_photos, total_collections,
            downloads, uploads_remaining, followers_count, following_count;
    public boolean followed_by_user;
    public ProfileImage profile_image;
    public Badge badge;
    public Links links;

    public User () {}

    protected User(Parcel in) {
        id = in.readString();
        username = in.readString();
        first_name = in.readString();
        last_name = in.readString();
        twitter_username = in.readString();
        portfolio_url = in.readString();
        bio = in.readString();
        location = in.readString();
        instagram_username = in.readString();
        email = in.readString();
        total_likes = in.readInt();
        total_photos = in.readInt();
        total_collections = in.readInt();
        downloads = in.readInt();
        uploads_remaining = in.readInt();
        followers_count = in.readInt();
        following_count = in.readInt();
        followed_by_user = in.readByte() != 0;
        profile_image = in.readParcelable(ProfileImage.class.getClassLoader());
        badge = in.readParcelable(Badge.class.getClassLoader());
        links = in.readParcelable(Links.class.getClassLoader());
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public String getFullName () {
        StringBuilder builder = new StringBuilder();
        if (!TextUtils.isEmpty(first_name)) {
            builder.append(first_name);
        }
        if (!TextUtils.isEmpty(last_name)) {
            builder.append(" ");
            builder.append(last_name);
        }
        if (TextUtils.isEmpty(builder)) {
            return username;
        }
        return builder.toString();
    }

    public boolean hasSocialMedia () {
        return hasEmail() || hasTwitter() || hasInstagram();
    }

    public boolean hasEmail () {
        return !TextUtils.isEmpty(email);
    }

    public boolean hasTwitter () {
        return !TextUtils.isEmpty(twitter_username);
    }

    public boolean hasInstagram () {
        return !TextUtils.isEmpty(instagram_username);
    }

    public String getTwitterUrl () {
        return "https://twitter.com/" + twitter_username;
    }

    public String getInstagramUrl () {
        return "https://www.instagram.com/" + instagram_username;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(username);
        parcel.writeString(first_name);
        parcel.writeString(last_name);
        parcel.writeString(twitter_username);
        parcel.writeString(portfolio_url);
        parcel.writeString(bio);
        parcel.writeString(location);
        parcel.writeString(instagram_username);
        parcel.writeString(email);
        parcel.writeInt(total_likes);
        parcel.writeInt(total_photos);
        parcel.writeInt(total_collections);
        parcel.writeInt(downloads);
        parcel.writeInt(uploads_remaining);
        parcel.writeInt(followers_count);
        parcel.writeInt(following_count);
        parcel.writeByte((byte) (followed_by_user ? 1 : 0));
        parcel.writeParcelable(profile_image, i);
        parcel.writeParcelable(badge, i);
        parcel.writeParcelable(links, i);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null && obj instanceof User) {
            return ((User)obj).id.equals(id);
        }
        return super.equals(obj);
    }
}
