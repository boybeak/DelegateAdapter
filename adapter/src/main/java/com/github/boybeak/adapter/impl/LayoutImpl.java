package com.github.boybeak.adapter.impl;

import android.support.annotation.LayoutRes;

import com.github.boybeak.adapter.AbsViewHolder;

/**
 * Created by boybe on 2016/12/21.
 */

public interface LayoutImpl<AVH extends AbsViewHolder> {

    @LayoutRes int getLayout ();
    Class<AVH> getHolderClass ();

}
