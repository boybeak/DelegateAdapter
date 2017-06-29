package com.nulldreams.delegateadapter.model;

/**
 * Created by gaoyunfei on 2016/12/18.
 */

public class User {

    private int avatar;
    private String name;
    private String description;

    public User (int avatar, String name, String description) {
        this.avatar = avatar;
        this.name = name;
        this.description = description;
    }

    public int getAvatar() {
        return avatar;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public void setAvatar(int avatar) {
        this.avatar = avatar;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
