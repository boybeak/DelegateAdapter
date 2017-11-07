package com.github.boybeak.timepaper.adapter.holder;

import android.content.Context;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.airbnb.lottie.LottieAnimationView;
import com.github.boybeak.adapter.AbsViewHolder;
import com.github.boybeak.adapter.DelegateAdapter;
import com.github.boybeak.timepaper.R;
import com.github.boybeak.timepaper.adapter.delegate.EmptyDelegate;

/**
 * Created by gaoyunfei on 2017/9/5.
 */

public class EmptyHolder extends AbsViewHolder<EmptyDelegate> {

    private LottieAnimationView lottieView;
    private View failedLayout;
    private AppCompatTextView detailTv;
    private AppCompatButton retryBtn;

    public EmptyHolder(View itemView) {
        super(itemView);

        lottieView = findViewById(R.id.empty_lottie);
        failedLayout = findViewById(R.id.empty_failed_layout);
        detailTv = findViewById(R.id.empty_failed_detail);
        retryBtn = findViewById(R.id.empty_retry_btn);
    }

    @Override
    public void onViewAttachedToWindow(DelegateAdapter adapter, Context context) {
        super.onViewAttachedToWindow(adapter, context);
        lottieView.playAnimation();
    }

    @Override
    public void onViewDetachedFromWindow(DelegateAdapter adapter, Context context) {
        super.onViewDetachedFromWindow(adapter, context);
        lottieView.cancelAnimation();
    }

    @Override
    public void onBindView(Context context, final EmptyDelegate emptyDelegate, final int position, final DelegateAdapter adapter) {

        if (emptyDelegate.isFailed()) {
            final String detail = emptyDelegate.getSource();
            failedLayout.setVisibility(View.VISIBLE);
            detailTv.setText(detail);
            retryBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    emptyDelegate.actionViewEvent(0, view, EmptyHolder.this, position, adapter);
                }
            });
        } else {
            failedLayout.setVisibility(View.GONE);
            retryBtn.setOnClickListener(null);
        }

    }
}
