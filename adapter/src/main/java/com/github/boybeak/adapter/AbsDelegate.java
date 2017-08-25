package com.github.boybeak.adapter;

import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.RequiresApi;
import android.util.Size;
import android.util.SizeF;
import android.util.SparseArray;

import com.github.boybeak.adapter.impl.DelegateImpl;

import java.io.Serializable;
import java.util.ArrayList;

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

    public Bundle bundle() {
        if (bundle == null) {
            bundle = new Bundle();
        }
        return bundle;
    }
}
