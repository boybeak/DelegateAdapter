package com.github.boybeak.adapter.extention;

import android.os.Bundle;

import com.github.boybeak.adapter.annotation.AnnotationDelegate;

/**
 * Created by gaoyunfei on 2017/8/27.
 */

public class CheckableDelegate<T> extends AnnotationDelegate<T> implements Checkable {

    private boolean checked;

    public CheckableDelegate(T t) {
        super(t);
    }

    public CheckableDelegate(T t, Bundle bundle) {
        super(t, bundle);
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
