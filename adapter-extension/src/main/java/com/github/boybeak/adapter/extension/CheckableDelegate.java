package com.github.boybeak.adapter.extension;

import android.os.Bundle;

import com.github.boybeak.adapter.AbsViewHolder;
import com.github.boybeak.adapter.OnViewEventListener;
import com.github.boybeak.adapter.annotation.AnnotationDelegate;

/**
 * Created by gaoyunfei on 2017/8/27.
 */

public class CheckableDelegate<Data, AVH extends AbsViewHolder> extends AnnotationDelegate<Data, AVH> implements Checkable {

    private boolean checked;

    public CheckableDelegate(Data data) {
        super(data);
    }

    public CheckableDelegate(Data data, Bundle bundle) {
        super(data, bundle);
    }

    public CheckableDelegate(Data data, OnViewEventListener<Data, AVH> viewEventListener) {
        super(data, viewEventListener);
    }

    public CheckableDelegate(Data data, Bundle bundle, OnViewEventListener<Data, AVH> viewEventListener) {
        super(data, bundle, viewEventListener);
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
