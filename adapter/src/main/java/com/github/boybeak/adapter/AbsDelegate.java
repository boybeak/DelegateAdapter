package com.github.boybeak.adapter;

import android.os.Bundle;
import android.view.View;

import com.github.boybeak.adapter.impl.DelegateImpl;

/**
 * Created by gaoyunfei on 16/7/29.
 */
public abstract class AbsDelegate<Data, AVH extends AbsViewHolder> implements DelegateImpl<Data, AVH> {

    private Data t;

    private Bundle bundle;

    private OnViewEventListener<Data, AVH> onViewEventListener;

    /**
     * @param t the source data item
     */
    public AbsDelegate (Data t) {
        this(t, null, null);
    }

    public AbsDelegate (Data t, Bundle bundle) {
        this(t, bundle, null);
    }

    public AbsDelegate (Data t, OnViewEventListener<Data, AVH> onViewEventListener) {
        this (t, null, onViewEventListener);
    }

    public AbsDelegate(Data t, Bundle bundle, OnViewEventListener<Data, AVH> onViewEventListener) {
        this.t = t;
        this.bundle = bundle;
        this.onViewEventListener = onViewEventListener;
    }

    public void setOnViewEventListener(OnViewEventListener<Data, AVH> onViewEventListener) {
        this.onViewEventListener = onViewEventListener;
    }

    @Override
    public Data getSource () {
        return t;
    }

    public void setSource (Data t) {
        this.t = t;
    }

    public Bundle bundle() {
        if (bundle == null) {
            bundle = new Bundle();
        }
        return bundle;
    }

    @Override
    public void actionViewEvent(int eventCode, View view, AVH viewHolder, Data t, int position, DelegateAdapter adapter) {
        if (onViewEventListener != null) {
            onViewEventListener.onViewEvent(eventCode, view, t, viewHolder, position, adapter);
        }
    }

    public interface OnViewEventListener<Data, AVH> {
        void onViewEvent (int eventCode, View view, Data t, AVH viewHolder, int position, DelegateAdapter adapter);
    }
}
