package com.github.boybeak.adapter;

import com.github.boybeak.adapter.impl.LayoutImpl;

/**
 * Created by gaoyunfei on 16/7/30.
 */
public interface DelegateReplace {
    LayoutImpl replaceWith (DelegateAdapter adapter, LayoutImpl impl);
}
