package com.nulldreams.adapter.annotation;

import android.content.Context;
import android.view.View;

import com.nulldreams.adapter.AbsViewHolder;
import com.nulldreams.adapter.DelegateAdapter;

/**
 * Created by gaoyunfei on 2017/2/28.
 */

final class NullHolder extends AbsViewHolder {

    private NullHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void onBindView(Context context, Object o, int position, DelegateAdapter adapter) {

    }
}
