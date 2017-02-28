package com.nulldreams.adapter.annotation;

import android.content.Context;
import android.view.View;

import com.nulldreams.adapter.DelegateAdapter;
import com.nulldreams.adapter.widget.OnItemLongClickListener;

/**
 * Created by gaoyunfei on 2017/2/27.
 */

final class NullOnItemLongClickListener<L, H> implements OnItemLongClickListener<L, H> {

    private NullOnItemLongClickListener() {

    }

    @Override
    public boolean onLongClick(View view, Context context, L l, H h, int position, DelegateAdapter adapter) {
        return false;
    }
}
