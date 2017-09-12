package com.nulldreams.delegateadapter;

import com.github.boybeak.adapter.AbsDelegate;

/**
 * Created by gaoyunfei on 2017/9/2.
 */

public class EmptyDelegate extends AbsDelegate<String, EmptyHolder> {

    public EmptyDelegate(String t) {
        super(t);
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
