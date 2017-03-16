package com.nulldreams.adapter.impl;

import android.support.annotation.LayoutRes;

import com.nulldreams.adapter.AbsViewHolder;
import com.nulldreams.adapter.widget.OnItemClickListener;
import com.nulldreams.adapter.widget.OnItemLongClickListener;

/**
 * Created by boybe on 2016/12/21.
 */

public interface LayoutImpl {

    @LayoutRes int getLayout ();
    Class<? extends AbsViewHolder> getHolderClass ();
    OnItemClickListener<LayoutImpl, AbsViewHolder> getOnItemClickListener ();
    OnItemLongClickListener<LayoutImpl, AbsViewHolder> getOnItemLongClickListener ();

    /**
     * @return ItemView's child view ids.
     * if {@link com.nulldreams.adapter.DelegateAdapter#ITEM_VIEW_ID} in this array,
     * the event will work on ItemView and it's child views;
     * return null or int[]{{@link com.nulldreams.adapter.DelegateAdapter#ITEM_VIEW_ID}},
     * the event will only work on ItemView.
     */
    int[] getOnClickIds ();

    /**
     * @return ItemView's child view ids.
     * if {@link com.nulldreams.adapter.DelegateAdapter#ITEM_VIEW_ID} in this array,
     * the event will work on ItemView and it's child views;
     * return null or int[]{{@link com.nulldreams.adapter.DelegateAdapter#ITEM_VIEW_ID}},
     * the event will only work on ItemView.
     */
    int[] getOnLongClickIds ();
}
