package com.nulldreams.adapter.widget;

import android.content.Context;
import android.view.View;

import com.nulldreams.adapter.DelegateAdapter;

/**
 * Created by gaoyunfei on 2017/2/26.
 */

public interface OnItemClickListener<L, H> {
    void onClick (View view, Context context, L l, H h,
                  int position, DelegateAdapter adapter);
}
