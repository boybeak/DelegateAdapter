package com.github.boybeak.adapter;

import com.github.boybeak.adapter.impl.LayoutImpl;

/**
 * Created by gaoyunfei on 16/7/30.
 */
public interface DelegateParser<T> {
    public LayoutImpl parse (DelegateAdapter adapter, T data);
}
