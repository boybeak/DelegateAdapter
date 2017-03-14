package com.nulldreams.adapter;

import android.os.Bundle;

import com.nulldreams.adapter.impl.DelegateImpl;

/**
 * Created by gaoyunfei on 16/7/29.
 */
public abstract class AbsDelegate<T> implements DelegateImpl {

    private T t;

    private Bundle bundle;

    /**
     * @param t the source data item
     */
    public AbsDelegate (T t) {
        this.t = t;
    }

    public AbsDelegate (T t, Bundle bundle) {
        this.t = t;
        this.bundle = bundle;
    }

    @Override
    public T getSource () {
        return t;
    }

    public void setSource (T t) {
        this.t = t;
    }

    private void ensureBundle () {
        if (bundle == null) {
            bundle = new Bundle();
        }
    }

    public void setBundle (Bundle bundle) {
        this.bundle = bundle;
    }

    public Bundle getBundle() {
        ensureBundle();
        return bundle;
    }
}
