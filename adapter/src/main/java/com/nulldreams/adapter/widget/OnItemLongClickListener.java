package com.nulldreams.adapter.widget;

import android.content.Context;
import android.view.View;

import com.nulldreams.adapter.DelegateAdapter;

/**
 * Created by gaoyunfei on 2017/2/27.
 */

public interface OnItemLongClickListener<L, H> {
    boolean onLongClick (View view, Context context, L l, H h,
                      int position, DelegateAdapter adapter);
}
