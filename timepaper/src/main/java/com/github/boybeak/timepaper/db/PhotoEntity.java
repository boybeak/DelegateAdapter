package com.github.boybeak.timepaper.db;

import com.github.boybeak.timepaper.model.Photo;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by gaoyunfei on 2017/9/12.
 */

@Table(name = "download_photo")
public class PhotoEntity {

    public static final int STATE_NONE = 0, STATE_STARTED = 1, STATE_FINISHED = 2;

    @Column(name = "id", isId = true) public String id;
    @Column(name = "state") public int state;

    public PhotoEntity () {}

    public PhotoEntity (Photo photo) {
        id = photo.id;
    }

}
