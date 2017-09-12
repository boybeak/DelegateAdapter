package com.github.boybeak.adapter.extention.state;

import android.os.Bundle;
import android.support.annotation.IntDef;

import com.github.boybeak.adapter.AbsDelegate;
import com.github.boybeak.adapter.AbsViewHolder;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by gaoyunfei on 2017/9/5.
 */

public abstract class StateDelegate<Data, AVH extends AbsViewHolder> extends AbsDelegate<Data, AVH> {

    public static final int EMPTY = 0, LOADING = 1, SUCCESS = 2, FAILED = 3;

    private int state;

    public StateDelegate(Data t) {
        super(t);
    }

    public StateDelegate(Data t, Bundle bundle) {
        super(t, bundle);
    }

    public StateDelegate(Data t, OnViewEventListener<Data, AVH> onViewEventListener) {
        super(t, onViewEventListener);
    }

    public StateDelegate(Data t, Bundle bundle, OnViewEventListener<Data, AVH> onViewEventListener) {
        super(t, bundle, onViewEventListener);
    }

    public int getState () {
        return state;
    }

    public void notifyEmptyState () {
        this.state = EMPTY;
    }

    public void notifyLoadingState () {
        this.state = LOADING;
    }

    public void notifySuccessState () {
        this.state = SUCCESS;
    }

    public void notifyFailedState () {
        this.state = FAILED;
    }

    public boolean isEmpty () {
        return this.state == EMPTY;
    }

    public boolean isLoading () {
        return this.state == LOADING;
    }

    public boolean isSuccess () {
        return this.state == SUCCESS;
    }

    public boolean isFailed () {
        return this.state == FAILED;
    }

    private void notifyState (int state) {
        this.state = state;
    }
}
