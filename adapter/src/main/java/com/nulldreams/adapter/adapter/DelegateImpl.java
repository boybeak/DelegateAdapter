package com.nulldreams.adapter.adapter;

import android.support.annotation.LayoutRes;

/**
 * Created by gaoyunfei on 16/7/29.
 */
public interface DelegateImpl<T> {
    public int getType ();
    public @LayoutRes int getLayout ();
    public T getSource ();
}
