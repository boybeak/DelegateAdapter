package com.nulldreams.delegateadapter.adapter.delegate;

import com.github.boybeak.adapter.annotation.AnnotationDelegate;
import com.github.boybeak.adapter.annotation.DelegateInfo;
import com.nulldreams.delegateadapter.R;
import com.nulldreams.delegateadapter.adapter.holder.StatusHolder;
import com.nulldreams.delegateadapter.model.Status;

/**
 * Created by gaoyunfei on 2017/6/29.
 */
@DelegateInfo(layoutID = R.layout.layout_status, holderClass = StatusHolder.class)
public class StatusDelegate extends AnnotationDelegate<Status> {
    public StatusDelegate(Status status) {
        super(status);
    }
}
