package com.nulldreams.delegateadapter.adapter.delegate;

import com.github.boybeak.adapter.DelegateAdapter;
import com.github.boybeak.adapter.annotation.AnnotationDelegate;
import com.github.boybeak.adapter.annotation.DelegateInfo;
import com.github.boybeak.adapter.annotation.OnClick;
import com.github.boybeak.adapter.annotation.OnLongClick;
import com.nulldreams.delegateadapter.R;
import com.nulldreams.delegateadapter.adapter.event.YtbClickListener;
import com.nulldreams.delegateadapter.adapter.event.YtbLongClickListener;
import com.nulldreams.delegateadapter.adapter.holder.YtbHolder;
import com.nulldreams.delegateadapter.model.Ytb;

/**
 * Created by gaoyunfei on 2016/12/22.
 */
@DelegateInfo(
        layoutID = R.layout.layout_ytb,
        holderClass = YtbHolder.class/*,
        onClick = YtbClickListener.class,
        onClickIds = {DelegateAdapter.ITEM_VIEW_ID, R.id.thumb}*//*,
        onLongClick = YtbLongClickListener.class,
        onLongClickIds = {DelegateAdapter.ITEM_VIEW_ID, R.id.thumb}*/
)
public class YtbDelegate extends AnnotationDelegate<Ytb>{

    @OnClick (ids = {DelegateAdapter.ITEM_VIEW_ID, R.id.thumb})
    public Class<YtbClickListener> clickListenerClass
            = YtbClickListener.class;

    @OnLongClick (ids = {DelegateAdapter.ITEM_VIEW_ID, R.id.thumb})
    public Class<YtbLongClickListener> longClickListenerClass
            = YtbLongClickListener.class;
    public YtbDelegate(Ytb ytb) {
        super(ytb);
    }
}
