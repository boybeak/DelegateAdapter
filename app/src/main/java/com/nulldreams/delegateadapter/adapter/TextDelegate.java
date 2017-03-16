package com.nulldreams.delegateadapter.adapter;

import android.content.Context;
import android.view.View;

import com.nulldreams.adapter.AbsViewHolder;
import com.nulldreams.adapter.DelegateAdapter;
import com.nulldreams.adapter.annotation.AnnotationDelegate;
import com.nulldreams.adapter.annotation.DelegateInfo;
import com.nulldreams.adapter.annotation.OnClick;
import com.nulldreams.adapter.annotation.OnLongClick;
import com.nulldreams.adapter.impl.LayoutImpl;
import com.nulldreams.adapter.widget.OnItemClickListener;
import com.nulldreams.delegateadapter.R;
import com.nulldreams.delegateadapter.adapter.event.TextClickListener;
import com.nulldreams.delegateadapter.adapter.event.TextLongClickListener;

/**
 * Created by gaoyunfei on 2016/12/18.
 */
@DelegateInfo(
        layoutID = R.layout.layout_text,
        holderClass = TextHolder.class/*,
        onClick = TextClickListener.class,
        onClickIds = {DelegateAdapter.ITEM_VIEW_ID, R.id.text_tv}*/)
public class TextDelegate extends AnnotationDelegate<String> {

    @OnClick(ids = {DelegateAdapter.ITEM_VIEW_ID, R.id.text_tv})
    public Class<TextClickListener> clickListenerClass
            = TextClickListener.class;

    @OnLongClick(ids = {DelegateAdapter.ITEM_VIEW_ID, R.id.text_tv})
    public Class<TextLongClickListener> longClickListenerClass = TextLongClickListener.class;

    public TextDelegate(String s) {
        super(s);
    }

}
