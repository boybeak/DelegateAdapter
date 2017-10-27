package com.github.boybeak.timepaper.adapter.delegate;

import com.github.boybeak.adapter.extension.state.StateDelegate;
import com.github.boybeak.timepaper.R;
import com.github.boybeak.timepaper.adapter.holder.EmptyHolder;

/**
 * Created by gaoyunfei on 2017/9/5.
 */

public class EmptyDelegate extends StateDelegate<String, EmptyHolder> {


    public EmptyDelegate(String t, OnViewEventListener<String, EmptyHolder> onViewEventListener) {
        super(t, onViewEventListener);
    }

    @Override
    public int getLayout() {
        return R.layout.layout_empty;
    }

    @Override
    public Class<EmptyHolder> getHolderClass() {
        return EmptyHolder.class;
    }
}
