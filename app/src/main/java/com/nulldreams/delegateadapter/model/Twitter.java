package com.nulldreams.delegateadapter.model;

import java.util.Random;

/**
 * Created by gaoyunfei on 2016/12/22.
 */

public class Twitter {
    private int profile, minutes, forwardCount, commentCount, likeCount;
    private String name, atName, text;

    public Twitter(int profile, String name, String text) {
        this.profile = profile;
        this.name = name;
        this.text = text;
        atName = "@" + name;

        Random random = new Random();

        minutes = random.nextInt(60);
        forwardCount = random.nextInt(1000);
        commentCount = random.nextInt(1000);
        likeCount = random.nextInt(1000);
    }

    public int getProfile() {
        return profile;
    }

    public int getMinutes() {
        return minutes;
    }

    public int getForwardCount() {
        return forwardCount;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public String getName() {
        return name;
    }

    public String getAtName() {
        return atName;
    }

    public String getText() {
        return text;
    }
}
