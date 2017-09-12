package com.github.boybeak.timepaper.adapter.delegate;

import com.github.boybeak.adapter.annotation.AnnotationDelegate;
import com.github.boybeak.adapter.annotation.DelegateInfo;
import com.github.boybeak.timepaper.R;
import com.github.boybeak.timepaper.adapter.holder.PhotoInfoHolder;
import com.github.boybeak.timepaper.model.Photo;

/**
 * Created by gaoyunfei on 2017/9/6.
 */
@DelegateInfo(layoutID = R.layout.layout_photo_info, holderClass = PhotoInfoHolder.class)
public class PhotoInfoDelegate extends AnnotationDelegate<Photo, PhotoInfoHolder> {
    public PhotoInfoDelegate(Photo photo) {
        super(photo);
    }
}
