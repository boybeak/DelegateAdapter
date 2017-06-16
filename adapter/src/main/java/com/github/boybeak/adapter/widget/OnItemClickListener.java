package com.github.boybeak.adapter.widget;

import android.content.Context;
import android.view.View;

import com.github.boybeak.adapter.DelegateAdapter;

/**
 * Created by gaoyunfei on 2017/2/26.
 */

public interface OnItemClickListener<L, H> {
    void onClick (View view, Context context, L l, H h,
                  int position, DelegateAdapter adapter);
}
