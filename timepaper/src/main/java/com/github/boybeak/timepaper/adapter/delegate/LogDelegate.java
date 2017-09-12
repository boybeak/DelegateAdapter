package com.github.boybeak.timepaper.adapter.delegate;

import com.github.boybeak.adapter.AbsDelegate;
import com.github.boybeak.timepaper.R;
import com.github.boybeak.timepaper.adapter.holder.LogHolder;

import java.util.Date;

/**
 * Created by gaoyunfei on 2017/9/4.
 */

public class LogDelegate extends AbsDelegate<String, LogHolder> {

    public LogDelegate(String t) {
        super(t);
    }

    public LogDelegate(String t, OnViewEventListener<String, LogHolder> onViewEventListener) {
        super(t, onViewEventListener);
    }

    public String getLogMsg () {
        return getSource();
    }

    @Override
    public int getLayout() {
        return R.layout.layout_log;
    }

    @Override
    public Class<LogHolder> getHolderClass() {
        return LogHolder.class;
    }
}
