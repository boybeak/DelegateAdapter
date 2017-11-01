package com.github.boybeak.adapter;

import android.os.Bundle;
import android.view.View;

import com.github.boybeak.adapter.impl.DelegateImpl;

/**
 * Created by gaoyunfei on 16/7/29.
 */
public abstract class AbsDelegate<Data, AVH extends AbsViewHolder> implements DelegateImpl<Data, AVH> {

    private Data data;

    private Bundle bundle;

    private OnViewEventListener<Data, AVH> onViewEventListener;

    /**
     * @param data the source data item
     */
    public AbsDelegate (Data data) {
        this(data, null, null);
    }

    public AbsDelegate (Data data, Bundle bundle) {
        this(data, bundle, null);
    }

    public AbsDelegate (Data data, OnViewEventListener<Data, AVH> onViewEventListener) {
        this (data, null, onViewEventListener);
    }

    public AbsDelegate(Data data, Bundle bundle, OnViewEventListener<Data, AVH> onViewEventListener) {
        this.data = data;
        this.bundle = bundle;
        this.onViewEventListener = onViewEventListener;
    }

    public void setOnViewEventListener(OnViewEventListener<Data, AVH> onViewEventListener) {
        this.onViewEventListener = onViewEventListener;
    }

    @Override
    public Data getSource () {
        return data;
    }

    public void setSource (Data data) {
        this.data = data;
    }

    public Bundle bundle() {
        if (bundle == null) {
            bundle = new Bundle();
        }
        return bundle;
    }

    @Override
    public void actionViewEvent(int eventCode, View view, AVH viewHolder, Data data,
                                int position, DelegateAdapter adapter) {
        if (onViewEventListener != null) {
            onViewEventListener.onViewEvent(eventCode, view, data, viewHolder, position, adapter);
        }
    }

    public interface OnViewEventListener<Data, AVH> {
        void onViewEvent (int eventCode, View view, Data data, AVH viewHolder,
                          int position, DelegateAdapter adapter);
    }
}
