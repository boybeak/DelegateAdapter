package com.nulldreams.delegateadapter.model;

/**
 * Created by gaoyunfei on 2017/6/29.
 */

public class Status {

    public User author;
    public String content;
    public boolean deletable;

    public Status(User author, String content) {
        this.author = author;
        this.content = content;
        deletable = false;
    }

    public Status(User author, String content, boolean deletable) {
        this.author = author;
        this.content = content;
        this.deletable = deletable;
    }
}
