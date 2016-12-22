package com.nulldreams.adapter;

import com.nulldreams.adapter.impl.DelegateImpl;

/**
 * Created by gaoyunfei on 16/7/29.
 */
public abstract class AbsDelegate<T> implements DelegateImpl {

    private T t;

    /**
     * @param t the source data item
     */
    public AbsDelegate (T t) {
        this.t = t;
    }

    @Override
    public T getSource () {
        return t;
    }

    public void setSource (T t) {
        this.t = t;
    }

}
