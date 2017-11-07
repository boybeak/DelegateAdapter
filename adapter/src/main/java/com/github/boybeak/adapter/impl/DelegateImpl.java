package com.github.boybeak.adapter.impl;

import android.os.Bundle;
import android.view.View;

import com.github.boybeak.adapter.AbsViewHolder;
import com.github.boybeak.adapter.DelegateAdapter;

/**
 * Created by gaoyunfei on 16/7/29.
 */
public interface DelegateImpl<Data, AVH extends AbsViewHolder> extends LayoutImpl<AVH> {
    Data getSource ();

    void actionViewEvent (int eventCode, View view, AVH viewHolder,
                          int position, DelegateAdapter adapter);
}
