package com.nulldreams.delegateadapter.model;

import android.content.Context;

import com.nulldreams.delegateadapter.R;

/**
 * Created by gaoyunfei on 2016/12/22.
 */

public class Ytb {
    private int profile, thumb;
    private String title, caption;

    public Ytb (Context context, int profile, int thumb, String title, String author, int viewCount) {
        this.profile = profile;
        this.thumb = thumb;
        this.title = title;
        this.caption = context.getString(R.string.ytb_rex, author, viewCount);
    }

    public int getProfile() {
        return profile;
    }

    public int getThumb() {
        return thumb;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCaption() {
        return caption;
    }

}
