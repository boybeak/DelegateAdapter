package com.nulldreams.adapter;

import com.nulldreams.adapter.impl.LayoutImpl;

/**
 * Created by gaoyunfei on 16/7/29.
 */
public interface DelegateFilter<T> {
    public boolean accept (DelegateAdapter adapter, LayoutImpl impl);
}
