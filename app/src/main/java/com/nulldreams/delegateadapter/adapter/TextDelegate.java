package com.nulldreams.delegateadapter.adapter;

import android.content.Context;
import android.view.View;

import com.nulldreams.adapter.AbsViewHolder;
import com.nulldreams.adapter.DelegateAdapter;
import com.nulldreams.adapter.annotation.AnnotationDelegate;
import com.nulldreams.adapter.annotation.DelegateInfo;
import com.nulldreams.adapter.impl.LayoutImpl;
import com.nulldreams.adapter.widget.OnItemClickListener;
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
