package com.github.boybeak.timepaper.adapter.delegate;

import com.github.boybeak.adapter.annotation.AnnotationDelegate;
import com.github.boybeak.adapter.annotation.DelegateInfo;
import com.github.boybeak.timepaper.R;
import com.github.boybeak.timepaper.adapter.holder.ExifHolder;
import com.github.boybeak.timepaper.model.Exif;

/**
 * Created by gaoyunfei on 2017/9/6.
 */

@DelegateInfo(layoutID = R.layout.layout_exif, holderClass = ExifHolder.class)
public class ExifDelegate extends AnnotationDelegate<Exif, ExifHolder> {

    public ExifDelegate(Exif exif) {
        super(exif);
    }
}
