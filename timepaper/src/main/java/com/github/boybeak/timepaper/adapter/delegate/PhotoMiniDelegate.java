package com.github.boybeak.timepaper.adapter.delegate;

import com.github.boybeak.adapter.OnViewEventListener;
import com.github.boybeak.adapter.annotation.AnnotationDelegate;
import com.github.boybeak.adapter.annotation.DelegateInfo;
import com.github.boybeak.timepaper.R;
import com.github.boybeak.timepaper.adapter.holder.PhotoMiniHolder;
import com.github.boybeak.timepaper.model.Photo;

/**
 * Created by gaoyunfei on 2017/9/9.
 */
@DelegateInfo(layoutID = R.layout.layout_photo_mini, holderClass = PhotoMiniHolder.class)
public class PhotoMiniDelegate extends AnnotationDelegate<Photo, PhotoMiniHolder> {

    public static final int EVENT_CLICK = 1, EVENT_LONG_CLICK = 2;

    public PhotoMiniDelegate(Photo photo) {
        super(photo);
    }

    public PhotoMiniDelegate(Photo photo, OnViewEventListener<Photo, PhotoMiniHolder> onViewEventListener) {
        super(photo, onViewEventListener);
    }
}
