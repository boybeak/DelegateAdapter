package com.github.boybeak.adapter.extension;

import com.github.boybeak.adapter.DelegateAdapter;
import com.github.boybeak.adapter.impl.LayoutImpl;
import com.github.boybeak.selector.Operator;
import com.github.boybeak.selector.Path;

/**
 * Created by gaoyunfei on 2017/8/27.
 */

public class SingleController implements Controller {

    private DelegateAdapter mAdapter;

    private Checkable mCurrentCheckable;

    private OnSingleCheckedListener onSingleListener;

    private boolean isStarted = false;

    public SingleController(DelegateAdapter adapter) {
        this.mAdapter = adapter;
    }

    @Override
    public void check(Checkable checkable) {
        if (!isStarted()) {
            return;
        }
        if (checkable == mCurrentCheckable) {
            return;
        }
        if (mCurrentCheckable != null) {
            mCurrentCheckable.setChecked(false);
            int index = mAdapter.indexOf((LayoutImpl) mCurrentCheckable);
            mAdapter.notifyItemChanged(index);
        }
        checkable.setChecked(true);

        int index = mAdapter.indexOf((LayoutImpl) checkable);
        mAdapter.notifyItemChanged(index);

        Checkable old = mCurrentCheckable;

        mCurrentCheckable = checkable;
        if (onSingleListener != null) {
            onSingleListener.onSingleChecked(checkable, old);
        }

    }

    @Override
    public void start() {
        isStarted = true;
        mAdapter.notifyDataSetChanged();
        if (onSingleListener != null) {
            onSingleListener.onControlStart();
        }
    }

    @Override
    public void stop() {
        isStarted = false;
        if (mCurrentCheckable != null) {
            mCurrentCheckable.setChecked(false);
        }
        mAdapter.notifyDataSetChanged();
        if (onSingleListener != null) {
            onSingleListener.onControlStop();
        }
    }

    @Override
    public boolean isStarted() {
        return isStarted;
    }

    @Override
    public boolean hasCheckedOnes() {
        return getCheckedCount() > 0;
    }

    @Override
    public int getCheckedCount() {
        return mAdapter.selector(Checkable.class).where(
                Path.with(Checkable.class, Boolean.class).methodWith("isChecked"),
                Operator.OPERATOR_EQUAL,
                true
        ).count();
    }

    public Checkable getCheckedOne () {
        return mCurrentCheckable;
    }

    public void setOnSingleCheckedListener(OnSingleCheckedListener onSingleListener) {
        this.onSingleListener = onSingleListener;
    }

    public interface OnSingleCheckedListener {
        void onControlStart ();
        void onSingleChecked (Checkable newCheckable, Checkable oldCheckable);
        void onControlStop ();
    }
}
