package com.nulldreams.delegateadapter.adapter.delegate;

import com.nulldreams.adapter.annotation.AnnotationDelegate;
import com.nulldreams.adapter.annotation.DelegateInfo;
import com.nulldreams.delegateadapter.R;
import com.nulldreams.delegateadapter.adapter.event.TwitterClickListener;
import com.nulldreams.delegateadapter.adapter.event.TwitterLongClickListener;
import com.nulldreams.delegateadapter.adapter.holder.TwitterHolder;
import com.nulldreams.delegateadapter.model.Twitter;

/**
 * Created by gaoyunfei on 2016/12/22.
 */
@DelegateInfo(
        layoutID = R.layout.layout_twitter,
        holderClass = TwitterHolder.class,
        onClick = TwitterClickListener.class,
        onLongClick = TwitterLongClickListener.class
)
public class TwitterDelegate extends AnnotationDelegate<Twitter>{
    public TwitterDelegate(Twitter twitter) {
        super(twitter);
    }
}
