package com.github.boybeak.timepaper.db;

import com.github.boybeak.timepaper.model.User;

import org.xutils.db.annotation.Table;

/**
 * Created by gaoyunfei on 2017/9/12.
 */

@Table(name = "download_photo")
public class PhotoEntity {

    public static final int STATE_NONE = 0, STATE_STARTED = 1, STATE_FINISHED = 2;

    public int id;
    public String photo_id;
    public int state;

    public PhotoEntity (User user) {
        photo_id = user.id;
    }

}
