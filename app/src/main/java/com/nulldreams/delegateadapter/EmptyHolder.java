package com.nulldreams.delegateadapter;

import android.content.Context;
import android.view.View;

import com.github.boybeak.adapter.AbsViewHolder;
import com.github.boybeak.adapter.DelegateAdapter;

/**
 * Created by gaoyunfei on 2017/9/2.
 */

public class EmptyHolder extends AbsViewHolder<EmptyDelegate> {

    public EmptyHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void onBindView(Context context, EmptyDelegate emptyDelegate, int position, DelegateAdapter adapter) {

    }
}
