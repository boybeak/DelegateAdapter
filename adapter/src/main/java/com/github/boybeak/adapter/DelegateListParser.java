package com.github.boybeak.adapter;

import com.github.boybeak.adapter.impl.LayoutImpl;

import java.util.List;

/**
 * Created by boybe on 2017/2/6.
 */

public interface DelegateListParser<T> {
    public List<LayoutImpl> parse (DelegateAdapter adapter, T data);
}