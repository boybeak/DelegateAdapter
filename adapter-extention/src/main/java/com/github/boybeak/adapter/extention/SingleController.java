package com.github.boybeak.adapter.extention;

import com.github.boybeak.adapter.DelegateAdapter;
import com.github.boybeak.adapter.impl.LayoutImpl;

/**
 * Created by gaoyunfei on 2017/8/27.
 */

public class SingleController implements Controller {

    private DelegateAdapter mAdapter;

    private Checkable mCurrentCheckable;

    private OnSingleCheckedListener onSingleListener;

    public SingleController(DelegateAdapter adapter) {
        this.mAdapter = adapter;
    }

    @Override
    public void check(Checkable checkable) {
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

    public Checkable getCheckedOne () {
        return mCurrentCheckable;
    }

    public void setOnSingleCheckedListener(OnSingleCheckedListener onSingleListener) {
        this.onSingleListener = onSingleListener;
    }

    public interface OnSingleCheckedListener {
        void onSingleChecked (Checkable newCheckable, Checkable oldCheckable);
    }
}
