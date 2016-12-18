package com.nulldreams.delegateadapter.adapter;

import com.nulldreams.adapter.adapter.AnnotationDelegate;
import com.nulldreams.adapter.annotation.DelegateInfo;
import com.nulldreams.delegateadapter.R;

/**
 * Created by gaoyunfei on 2016/12/18.
 */
@DelegateInfo(layoutID = R.layout.layout_text, holderClass = TextHolder.class)
public class TextDelegate extends AnnotationDelegate<String> {

    public TextDelegate(String s) {
        super(s);
    }
}
