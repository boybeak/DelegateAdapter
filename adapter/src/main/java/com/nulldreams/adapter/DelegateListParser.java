package com.nulldreams.adapter;

import com.nulldreams.adapter.impl.LayoutImpl;

import java.util.List;

/**
 * Created by boybe on 2017/2/6.
 */

public interface DelegateListParser<T> {
    public List<LayoutImpl> parse (T data);
}