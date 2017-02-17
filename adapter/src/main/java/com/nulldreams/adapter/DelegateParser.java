package com.nulldreams.adapter;

import com.nulldreams.adapter.impl.LayoutImpl;

/**
 * Created by gaoyunfei on 16/7/30.
 */
public interface DelegateParser<T> {
    public LayoutImpl parse (DelegateAdapter adapter, T data);
}
