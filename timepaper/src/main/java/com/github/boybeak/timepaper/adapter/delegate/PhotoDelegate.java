package com.github.boybeak.timepaper.adapter.delegate;

import com.github.boybeak.adapter.AbsDelegate;
import com.github.boybeak.timepaper.R;
import com.github.boybeak.timepaper.adapter.holder.PhotoHolder;
import com.github.boybeak.timepaper.model.Photo;

/**
 * Created by gaoyunfei on 2017/9/5.
 */

public class PhotoDelegate extends AbsDelegate<Photo, PhotoHolder> {

    public static final int EVENT_CLICK = 1, EVENT_LONG_CLICK = 2;

    public PhotoDelegate(Photo t) {
        super(t);
    }

    @Override
    public int getLayout() {
        return R.layout.layout_photo;
    }

    @Override
    public Class<PhotoHolder> getHolderClass() {
        return PhotoHolder.class;
    }
}
