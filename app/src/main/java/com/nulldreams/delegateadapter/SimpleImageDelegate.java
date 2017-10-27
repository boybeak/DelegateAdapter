package com.nulldreams.delegateadapter;

import com.github.boybeak.adapter.annotation.DelegateInfo;
import com.github.boybeak.adapter.extension.CheckableDelegate;

/**
 * Created by gaoyunfei on 2017/8/30.
 */
@DelegateInfo(layoutID = R.layout.layout_thumb, holderClass = SimpleImageHolder.class)
public class SimpleImageDelegate extends CheckableDelegate<SimpleImage, SimpleImageHolder> {

    public SimpleImageDelegate(SimpleImage simpleImage) {
        super(simpleImage);
    }

    public SimpleImageDelegate(SimpleImage simpleImage, OnViewEventListener<SimpleImage, SimpleImageHolder> onViewEventListener) {
        super(simpleImage, onViewEventListener);
    }
}
