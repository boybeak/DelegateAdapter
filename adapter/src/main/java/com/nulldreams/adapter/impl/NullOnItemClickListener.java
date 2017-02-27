package com.nulldreams.adapter.impl;

import android.content.Context;
import android.view.View;

import com.nulldreams.adapter.DelegateAdapter;
import com.nulldreams.adapter.widget.OnItemClickListener;

/**
 * Created by gaoyunfei on 2017/2/27.
 */

public final class NullOnItemClickListener<L, H> implements OnItemClickListener<L, H> {

    private NullOnItemClickListener() {

    }

    @Override
    public void onClick(View view, Context context, L o, H o2, int position, DelegateAdapter adapter) {

    }
}
