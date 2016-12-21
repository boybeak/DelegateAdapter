package com.nulldreams.adapter;

import com.nulldreams.adapter.impl.DelegateImpl;

/**
 * Created by gaoyunfei on 16/7/30.
 */
public interface DelegateParser<T> {
    public DelegateImpl parse (T data);
}
