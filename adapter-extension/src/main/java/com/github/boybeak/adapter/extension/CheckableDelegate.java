package com.github.boybeak.adapter.extension;

import android.os.Bundle;

import com.github.boybeak.adapter.AbsViewHolder;
import com.github.boybeak.adapter.annotation.AnnotationDelegate;

/**
 * Created by gaoyunfei on 2017/8/27.
 */

public class CheckableDelegate<T, AVH extends AbsViewHolder> extends AnnotationDelegate<T, AVH> implements Checkable {

    private boolean checked;

    public CheckableDelegate(T t) {
        super(t);
    }

    public CheckableDelegate(T t, Bundle bundle) {
        super(t, bundle);
    }

    public CheckableDelegate(T t, OnViewEventListener<T, AVH> onViewEventListener) {
        super(t, onViewEventListener);
    }

    public CheckableDelegate(T t, Bundle bundle, OnViewEventListener<T, AVH> onViewEventListener) {
        super(t, bundle, onViewEventListener);
    }


    @Override
    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    @Override
    public boolean isChecked() {
        return checked;
    }
}
