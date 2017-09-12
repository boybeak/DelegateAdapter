package com.nulldreams.delegateadapter;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.ProgressBar;

import com.github.boybeak.adapter.AbsViewHolder;
import com.github.boybeak.adapter.DelegateAdapter;

/**
 * Created by gaoyunfei on 2017/9/3.
 */

public class FooterHolder extends AbsViewHolder<FooterDelegate> {

    private ProgressBar pb;
    private AppCompatTextView msgTv;

    public FooterHolder(View itemView) {
        super(itemView);

    }

    @Override
    public void onBindView(Context context, final FooterDelegate footerDelegate, final int position, final DelegateAdapter adapter) {
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                footerDelegate.actionViewEvent(0, view, FooterHolder.this, footerDelegate.getSource(), position, adapter);
            }
        });
    }
}
