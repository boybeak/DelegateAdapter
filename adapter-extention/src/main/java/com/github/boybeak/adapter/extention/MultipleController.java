package com.github.boybeak.adapter.extention;

import com.github.boybeak.adapter.DelegateAdapter;
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

    public MultipleController(DelegateAdapter adapter) {
        this.mAdapter = adapter;
    }

    public void checkAll () {
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
        mAdapter.selector(Checkable.class).map(new Action<Checkable>() {
            @Override
            public void action(int index, Checkable checkable) {
                if (checkable.isChecked()) {
                    checkable.setChecked(false);
                }
            }
        });
        mAdapter.notifyDataSetChanged();
        if (onMultipleListener != null && isAllUnchecked()) {
            onMultipleListener.onAllUnchecked(mAdapter.selector(Checkable.class).findAll());
        }
    }

    public boolean isAllChecked() {
        int allCheckableCount = mAdapter.selector(Checkable.class).count();
        if (allCheckableCount == 0) {
            return false;
        }
        int checkedCount = mAdapter.selector(Checkable.class).where(
                Path.with(Checkable.class, Boolean.class).methodWith("isChecked"),
                Operator.OPERATOR_EQUAL,
                true
        ).count();
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
        checkable.setChecked(!checkable.isChecked());
        if (onMultipleListener != null) {
            onMultipleListener.onCheckChanged(checkable, isAllChecked());
        }
    }

    public void setOnMultipleCheckedListener(OnMultipleCheckedListener onMultipleListener) {
        this.onMultipleListener = onMultipleListener;
    }

    public interface OnMultipleCheckedListener {
        void onCheckChanged (Checkable checkable, boolean isAllChecked);
        void onAllChecked (List<Checkable> checkables);
        void onAllUnchecked (List<Checkable> checkables);
    }
}
