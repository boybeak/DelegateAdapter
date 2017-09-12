package com.github.boybeak.timepaper.adapter;

import android.support.v7.widget.GridLayoutManager;

import com.github.boybeak.adapter.DelegateAdapter;
import com.github.boybeak.adapter.impl.LayoutImpl;
import com.github.boybeak.timepaper.adapter.delegate.EmptyDelegate;
import com.github.boybeak.timepaper.adapter.delegate.FooterDelegate;

/**
 * Created by gaoyunfei on 2017/9/9.
 */

public class StateSpanLookup extends GridLayoutManager.SpanSizeLookup {

    private DelegateAdapter mAdapter = null;
    private int mSpanCount;
    public StateSpanLookup(DelegateAdapter adapter, int spanCount) {
        mAdapter = adapter;
        mSpanCount = spanCount;
    }

    @Override
    public int getSpanSize(int position) {
        LayoutImpl impl = mAdapter.get(position);
        if (impl instanceof EmptyDelegate || impl instanceof FooterDelegate) {
            return mSpanCount;
        }
        return 1;
    }
}
