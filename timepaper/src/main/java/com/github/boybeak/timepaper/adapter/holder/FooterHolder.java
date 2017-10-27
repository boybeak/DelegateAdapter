package com.github.boybeak.timepaper.adapter.holder;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.ProgressBar;

import com.github.boybeak.adapter.AbsViewHolder;
import com.github.boybeak.adapter.DelegateAdapter;
import com.github.boybeak.adapter.extension.state.StateDelegate;
import com.github.boybeak.timepaper.R;
import com.github.boybeak.timepaper.adapter.delegate.FooterDelegate;

/**
 * Created by gaoyunfei on 2017/9/4.
 */

public class FooterHolder extends AbsViewHolder<FooterDelegate> {

    private ProgressBar pb;
    private AppCompatTextView msgTv;

    public FooterHolder(View itemView) {
        super(itemView);

        pb = findViewById(R.id.footer_pb);
        msgTv = findViewById(R.id.footer_msg);
    }

    @Override
    public void onBindView(Context context, FooterDelegate footerDelegate, int position, DelegateAdapter adapter) {
        int state = footerDelegate.getState();
        switch (state) {
            case StateDelegate.EMPTY:
                msgTv.setText(R.string.text_footer_empty);
                msgTv.setVisibility(View.VISIBLE);
                pb.setVisibility(View.GONE);
                break;
            case StateDelegate.LOADING:
                msgTv.setText(null);
                msgTv.setVisibility(View.GONE);
                pb.setVisibility(View.VISIBLE);
                break;
            case StateDelegate.SUCCESS:
                msgTv.setText(R.string.text_footer_success);
                msgTv.setVisibility(View.VISIBLE);
                pb.setVisibility(View.GONE);
                break;
            case StateDelegate.FAILED:
                msgTv.setText(R.string.text_footer_failed);
                msgTv.setVisibility(View.VISIBLE);
                pb.setVisibility(View.GONE);
                break;
        }
    }
}
