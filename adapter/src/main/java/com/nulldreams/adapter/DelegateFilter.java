package com.nulldreams.adapter;

/**
 * Created by gaoyunfei on 16/7/29.
 */
public interface DelegateFilter<T> {
    public boolean accept (DelegateImpl impl);
}
