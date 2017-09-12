package com.github.boybeak.timepaper.adapter.holder;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;

import com.github.boybeak.adapter.AbsViewHolder;
import com.github.boybeak.adapter.DelegateAdapter;
import com.github.boybeak.timepaper.R;
import com.github.boybeak.timepaper.adapter.delegate.LogDelegate;

/**
 * Created by gaoyunfei on 2017/9/4.
 */

public class LogHolder extends AbsViewHolder<LogDelegate> {

    private AppCompatTextView logTv;

    public LogHolder(View itemView) {
        super(itemView);

        logTv = findViewById(R.id.log_tv);
    }

    @Override
    public void onBindView(Context context, final LogDelegate logDelegate, final int position, final DelegateAdapter adapter) {

        final String logMsg = logDelegate.getLogMsg();

        logTv.setText(logMsg);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logDelegate.actionViewEvent(0, view, LogHolder.this, logMsg, position, adapter);
            }
        });
    }
}
