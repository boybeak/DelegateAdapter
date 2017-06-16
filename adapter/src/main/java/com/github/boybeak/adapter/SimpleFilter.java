package com.github.boybeak.adapter;

import com.github.boybeak.adapter.impl.DelegateImpl;
import com.github.boybeak.adapter.impl.LayoutImpl;

/**
 * Created by boybe on 2016/8/16.
 */
public class SimpleFilter<T> implements DelegateFilter<T> {

    private Class<T> tClz;

    public SimpleFilter (Class<T> clz) {
        tClz = clz;
    }

    @Override
    public boolean accept(DelegateAdapter adapter, LayoutImpl impl) {
        return impl != null && impl instanceof DelegateImpl && tClz.isInstance(((DelegateImpl)impl).getSource());
    }
}
