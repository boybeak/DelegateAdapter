package com.github.boybeak.adapter.extension;

import com.github.boybeak.adapter.DelegateAdapter;
import com.github.boybeak.adapter.impl.LayoutImpl;
import com.github.boybeak.selector.Action;
import com.github.boybeak.selector.Operator;
import com.github.boybeak.selector.Path;

import java.util.List;

/**
 * Created by gaoyunfei on 2017/8/27.
 */

public class MultipleController implements Controller {

    private DelegateAdapter mAdapter;

    private OnMultipleCheckedListener onMultipleListener;

    private boolean isStarted = false;

    public MultipleController(DelegateAdapter adapter) {
        this.mAdapter = adapter;
    }

    public void checkAll () {
        if (!isStarted()) {
            return;
        }
        mAdapter.selector(Checkable.class).map(new Action<Checkable>() {
            @Override
            public void action(int index, Checkable checkable) {
                if (!checkable.isChecked()) {
                    checkable.setChecked(true);
                }
            }
        });
        mAdapter.notifyDataSetChanged();
        if (onMultipleListener != null && isAllChecked()) {
            onMultipleListener.onAllChecked(mAdapter.selector(Checkable.class).findAll());
        }
    }

    public void uncheckAll () {
        if (!isStarted()) {
            return;
        }
        mAdapter.selector(Checkable.class).map(new Action<Checkable>() {
            @Override
            public void action(int index, Checkable checkable) {
                if (checkable.isChecked()) {
                    checkable.setChecked(false);
                }
            }
        });
        mAdapter.notifyDataSetChanged();
        if (onMultipleListener != null && isAllUnchecked() && isStarted()) {
            onMultipleListener.onAllUnchecked(mAdapter.selector(Checkable.class).findAll());
        }
    }

    public boolean isAllChecked() {
        int allCheckableCount = mAdapter.selector(Checkable.class).count();
        if (allCheckableCount == 0) {
            return false;
        }
        int checkedCount = getCheckedCount();
        return allCheckableCount == checkedCount;
    }

    public boolean isAllUnchecked () {
        int allCheckableCount = mAdapter.selector(Checkable.class).count();
        if (allCheckableCount == 0) {
            return false;
        }
        int uncheckedCount = mAdapter.selector(Checkable.class).where(
                Path.with(Checkable.class, Boolean.class).methodWith("isChecked"),
                Operator.OPERATOR_EQUAL,
                false
        ).count();
        return allCheckableCount == uncheckedCount;
    }

    public List<Checkable> getAllCheckedOnes () {
        return mAdapter.selector(Checkable.class).where(
                Path.with(Checkable.class, Boolean.class).methodWith("isChecked"),
                Operator.OPERATOR_EQUAL,
                true
        ).findAll();
    }

    @Override
    public void check(Checkable checkable) {
        if (!isStarted()) {
            return;
        }
        checkable.setChecked(!checkable.isChecked());
        int index = mAdapter.indexOf((LayoutImpl) checkable);
        mAdapter.notifyItemChanged(index);
        if (onMultipleListener != null) {
            onMultipleListener.onCheckChanged(checkable, isAllChecked());
        }
    }

    @Override
    public void start() {
        isStarted = true;
        mAdapter.notifyDataSetChanged();
        if (onMultipleListener != null) {
            onMultipleListener.onControlStart();
        }
    }

    @Override
    public void stop() {
        isStarted = false;
        mAdapter.selector(Checkable.class).map(new Action<Checkable>() {
            @Override
            public void action(int i, Checkable checkable) {
                checkable.setChecked(false);
            }
        });
        mAdapter.notifyDataSetChanged();
        if (onMultipleListener != null) {
            onMultipleListener.onControlStop();
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

    public void setOnMultipleCheckedListener(OnMultipleCheckedListener onMultipleListener) {
        this.onMultipleListener = onMultipleListener;
    }

    public interface OnMultipleCheckedListener {
        void onControlStart ();
        void onCheckChanged (Checkable checkable, boolean isAllChecked);
        void onAllChecked (List<Checkable> checkables);
        void onAllUnchecked (List<Checkable> checkables);
        void onControlStop ();
    }
}
