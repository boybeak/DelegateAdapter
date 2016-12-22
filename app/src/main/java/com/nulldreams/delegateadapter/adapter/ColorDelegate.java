package com.nulldreams.delegateadapter.adapter;

import com.nulldreams.adapter.annotation.AnnotationDelegate;
import com.nulldreams.adapter.annotation.DelegateInfo;
import com.nulldreams.delegateadapter.R;

/**
 * Created by gaoyunfei on 2016/12/18.
 */
@DelegateInfo(layoutID = R.layout.layout_color, holderClass = ColorHolder.class)
public class ColorDelegate extends AnnotationDelegate<Integer> {
    public ColorDelegate(Integer integer) {
        super(integer);
    }
}
