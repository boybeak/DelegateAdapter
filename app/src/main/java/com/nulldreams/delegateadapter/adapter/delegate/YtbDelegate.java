package com.nulldreams.delegateadapter.adapter.delegate;

import com.nulldreams.adapter.annotation.AnnotationDelegate;
import com.nulldreams.adapter.annotation.DelegateInfo;
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
        holderClass = YtbHolder.class,
        onClick = YtbClickListener.class,
        onLongClick = YtbLongClickListener.class
)
public class YtbDelegate extends AnnotationDelegate<Ytb>{
    public YtbDelegate(Ytb ytb) {
        super(ytb);
    }
}
