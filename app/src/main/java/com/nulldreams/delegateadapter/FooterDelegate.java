package com.nulldreams.delegateadapter;

import com.github.boybeak.adapter.AbsDelegate;

/**
 * Created by gaoyunfei on 2017/9/3.
 */

public class FooterDelegate extends AbsDelegate<String, FooterHolder> {

    public FooterDelegate(String t, OnViewEventListener<String, FooterHolder> onViewEventListener) {
        super(t, onViewEventListener);
    }

    @Override
    public int getLayout() {
        return R.layout.layout_footer;
    }

    @Override
    public Class<FooterHolder> getHolderClass() {
        return FooterHolder.class;
    }
}
