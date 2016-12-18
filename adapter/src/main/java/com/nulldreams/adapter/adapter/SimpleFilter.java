package com.nulldreams.adapter.adapter;

/**
 * Created by boybe on 2016/8/16.
 */
public class SimpleFilter<T> implements DelegateFilter<T> {

    private Class<T> tClz;

    public SimpleFilter (Class<T> clz) {
        tClz = clz;
    }

    @Override
    public boolean accept(DelegateImpl impl) {
        return impl != null && tClz.isInstance(impl.getSource());
    }
}
