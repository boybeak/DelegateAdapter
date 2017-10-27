package com.github.boybeak.timepaper.adapter.delegate;

import com.github.boybeak.adapter.AbsDelegate;
import com.github.boybeak.adapter.extension.state.StateDelegate;
import com.github.boybeak.timepaper.R;
import com.github.boybeak.timepaper.adapter.holder.FooterHolder;

/**
 * Created by gaoyunfei on 2017/9/4.
 */

public class FooterDelegate extends StateDelegate<String, FooterHolder> {

    public FooterDelegate(String t) {
        super(t);
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
