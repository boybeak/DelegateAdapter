package com.github.boybeak.adapter.annotation;

import android.content.Context;
import android.view.View;

import com.github.boybeak.adapter.AbsViewHolder;
import com.github.boybeak.adapter.DelegateAdapter;

/**
 * Created by gaoyunfei on 2017/2/28.
 */

public final class NullHolder extends AbsViewHolder {

    private NullHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void onBindView(Context context, Object o, int position, DelegateAdapter adapter) {

    }
}
