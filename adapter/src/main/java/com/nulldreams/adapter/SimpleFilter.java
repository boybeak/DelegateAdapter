package com.nulldreams.adapter;

import com.nulldreams.adapter.impl.DelegateImpl;
import com.nulldreams.adapter.impl.LayoutImpl;

/**
 * Created by boybe on 2016/8/16.
 */
public class SimpleFilter<T> implements DelegateFilter<T> {

    private Class<T> tClz;

    public SimpleFilter (Class<T> clz) {
        tClz = clz;
    }

    @Override
    public boolean accept(LayoutImpl impl) {
        return impl != null && impl instanceof DelegateImpl && tClz.isInstance(((DelegateImpl)impl).getSource());
    }
}
