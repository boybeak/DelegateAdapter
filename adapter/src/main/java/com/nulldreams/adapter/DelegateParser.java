package com.nulldreams.adapter;

/**
 * Created by gaoyunfei on 16/7/30.
 */
public interface DelegateParser<T> {
    public DelegateImpl parse (T data);
}
