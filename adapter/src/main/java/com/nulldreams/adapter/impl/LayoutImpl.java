package com.nulldreams.adapter.impl;

import android.support.annotation.LayoutRes;

import com.nulldreams.adapter.AbsViewHolder;
import com.nulldreams.adapter.widget.OnItemClickListener;

/**
 * Created by boybe on 2016/12/21.
 */

public interface LayoutImpl {
    @LayoutRes int getLayout ();
    Class<? extends AbsViewHolder> getHolderClass ();
    OnItemClickListener<LayoutImpl, AbsViewHolder> getOnItemClickListener ();
}
