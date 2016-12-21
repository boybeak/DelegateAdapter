package com.nulldreams.adapter;

import android.support.annotation.LayoutRes;

/**
 * Created by gaoyunfei on 16/7/29.
 */
public interface DelegateImpl<T> {
    int getType ();
    @LayoutRes int getLayout ();
    T getSource ();
}
