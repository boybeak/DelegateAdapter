package com.github.boybeak.adapter.extension;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.github.boybeak.adapter.DataChange;
import com.github.boybeak.adapter.DelegateAdapter;
import com.github.boybeak.adapter.DelegateListParser;
import com.github.boybeak.adapter.impl.LayoutImpl;

import java.util.Collection;
import java.util.List;

/**
 * Created by gaoyunfei on 2017/8/28.
 */

public class SuperAdapter<Empty extends LayoutImpl, Tail extends LayoutImpl> extends DelegateAdapter {

    private Controller mController;

    private Empty empty;
    private Tail tail;

    public SuperAdapter(Context context) {
        super(context);
    }

    public SuperAdapter(Context context, Bundle bundle) {
        super(context, bundle);
    }

    public SuperAdapter(Context context, Bundle bundle, List<LayoutImpl> dataLayoutList) {
        super(context, bundle, dataLayoutList);
    }

    public void setEmptyItem (Empty empty) {
        this.empty = empty;
        if (onlyContainsTailItem()) {
            super.remove(0).autoNotify();
        }
        if (isEmpty()) {
            super.add(empty).autoNotify();
        }
    }

    public Empty getEmptyItem() {
        return empty;
    }

    public boolean hasEmptyItem () {
        return empty != null;
    }

    public void setTailItem(Tail footer) {
        this.tail = footer;
        if (!hasEmptyItem()) {
            super.add(tail).autoNotify();
        }
    }

    public Tail getTailItem() {
        return tail;
    }

    public boolean hasTailItem() {
        return  tail != null;
    }

    public boolean containsEmptyItem () {
        return hasEmptyItem() && contains(empty);
    }

    private boolean removeEmptyItemIfNeed() {
        if (containsEmptyItem()) {
            super.remove(empty).autoNotify();
            return true;
        }
        return false;
    }

    public boolean onlyContainsTailItem() {
        return containsTailItem() && getItemCount() == 1;
    }

    public boolean onlyContainsEmptyItem () {
        return containsEmptyItem() && getItemCount() == 1;
    }

    private boolean manageEmptyState() {
        if (onlyContainsTailItem()) {
            if (hasEmptyItem()) {
                super.remove(0).autoNotify();
                super.add(empty).autoNotify();
            }
            return true;
        }
        return false;
    }

    public boolean containsTailItem() {
        return hasTailItem() && contains(tail);
    }

    public boolean isEmptyExceptEmptyAndTail() {
        return getItemCountExceptEmptyAndTail() == 0;
    }

    public int getItemCountExceptEmptyAndTail() {
        int count = getItemCount();
        if (containsEmptyItem()) {
            count--;
        }
        if (containsTailItem()) {
            count--;
        }
        return count;
    }

    @Override
    public DataChange add(LayoutImpl impl) {
        removeEmptyItemIfNeed();
        DataChange change;
        if (containsTailItem()) {
            change = super.add(getItemCount() - 1, impl);
        } else {
            super.add(impl);
            int changeCount = 1;
            if (hasTailItem()) {
                super.add(tail);
                changeCount++;
            }
            change = new DataChange(this, getItemCount() - changeCount, changeCount, DataChange.TYPE_ITEM_INSERTED);
        }
        return change;
    }

    @Override
    public <T> DataChange add(int position, T t, DelegateListParser<T> parser) {
        return super.add(position, t, parser);
    }

    @Override
    public DataChange add(int position, @NonNull LayoutImpl impl) {
        removeEmptyItemIfNeed();
        DataChange change;
        if (containsTailItem() && position == getItemCount()) {
            change = super.add(position - 1, impl);
        } else {
            if (position >= getItemCount()) {
                throw new IndexOutOfBoundsException("you can not add elements after the footer element.");
            }
            super.add(position, impl);
            if (hasTailItem()) {
                super.add(tail);
            }
            change = DataChange.notifyDataSetChangeInstance(this);
        }
        return change;
    }

    @Override
    public DataChange addAll(@NonNull Collection<? extends LayoutImpl> list) {
        if (list.isEmpty()) {
            //TODO need tell empty state if need
            if (isEmptyExceptEmptyAndTail()) {
                //TODO need tell empty state if need
                return DataChange.doNothingInstance();
            } else {
                if (!containsTailItem() && hasTailItem()) {
                    return super.add(tail);
                }
                //TODO tell footer empty if need
                //TODO return footer {@link DataChange}
            }
        } else {
            removeEmptyItemIfNeed();
            DataChange change = null;
            if (containsTailItem()) {
                change = super.addAll(getItemCount() - 1, list);
            } else {
                int changeCount = list.size();
                super.addAll(list);
                if (hasTailItem()) {
                    super.add(tail);
                    changeCount++;
                }
                change = new DataChange(this, getItemCount() - changeCount, changeCount, DataChange.TYPE_ITEM_RANGE_INSERTED);
            }
            return change;
        }
        return DataChange.doNothingInstance();
    }

    @Override
    public DataChange addAll(int position, @NonNull Collection<? extends LayoutImpl> list) {
        if (list.isEmpty()) {
            if (isEmptyExceptEmptyAndTail()) {
                return DataChange.doNothingInstance();
            } else {
                if (!containsTailItem() && hasTailItem()) {
                    return super.add(tail);
                } else {
                    return DataChange.doNothingInstance();
                }
            }
        } else {
            removeEmptyItemIfNeed();
            if (containsTailItem()) {
                if (position >= getItemCount()) {
                    throw new IndexOutOfBoundsException("you can not add elements after the footer element.");
                }
                int index = indexOf(tail);
                if (index == position) {
                    return super.addAll(position - 1, list);
                } else {
                    return super.addAll(position, list);
                }
            } else {
                super.addAll(position, list);
                if (hasTailItem()) {
                    super.add(tail);
                }
                return DataChange.notifyDataSetChangeInstance(this);
            }
//            super.addAll()
        }
    }

    @Override
    public DataChange remove(int position) {
        DataChange change = super.remove(position);
        if (manageEmptyState()) {
            return DataChange.notifyDataSetChangeInstance(this);
        }
        return change;
    }

    @Override
    public DataChange remove(LayoutImpl impl) {
        DataChange change = super.remove(impl);
        if (manageEmptyState()) {
            return DataChange.notifyDataSetChangeInstance(this);
        }
        return change;
    }

    @Override
    public DataChange clear() {
        super.clear();
        manageEmptyState();
        return DataChange.notifyDataSetChangeInstance(this);
    }

    public void notifyEmpty () {
        if (containsEmptyItem()) {
            notifyItemChanged(indexOf(empty));
        }
    }

    public void notifyTail () {
        if (containsTailItem()) {
            notifyItemChanged(indexOf(tail));
        }
    }

    @Override
    public long getItemId(int position) {
        if (hasStableIds()) {
            return position;
        }
        return super.getItemId(position);
    }

    public void notifyDataSetChangedSafety() {
        if (isEmptyExceptEmptyAndTail()) {
            if (hasEmptyItem()) {
                super.add(0, empty);
                if (containsTailItem()) {
                    super.remove(tail);
                }
            }

        } else {
            if (hasTailItem()) {
                if (containsTailItem()) {
                    if (!endWith(tail)) {
                        super.remove(tail);
                        super.add(tail);
                    }
                } else {
                    super.add(tail);
                }
            }
        }
        notifyDataSetChanged();
    }

    /**
     * go into under control single selection mode, you can choose items.
     * @return {@link SingleController}
     */
    public SingleController singleControl () {
        if (mController != null && mController instanceof SingleController) {
            return (SingleController)mController;
        }
        SingleController singleController = new SingleController(this);
        mController = singleController;
        notifyDataSetChanged();
        return singleController;
    }

    /**
     * go into under control multiple selection mode, you can choose items.
     * @return {@link MultipleController}
     */
    public MultipleController multipleControl () {
        if (mController != null && mController instanceof MultipleController) {
            return (MultipleController)mController;
        }
        MultipleController multipleController = new MultipleController(this);
        mController = multipleController;
        notifyDataSetChanged();
        return multipleController;
    }

    /**
     * check if already under control
     * @return true if already under control.
     */
    public boolean isUnderControl () {
        return mController != null;
    }

    /*private  <C extends Controller> C takeControlWith (C c) {
        if (isUnderControl() && c.getClass().isInstance(mController)) {
            return (C)mController;
        }
        mController = c;
        notifyDataSetChanged();
        return c;
    }*/

    /**
     * release the control mode
     */
    /*public void dismissControl () {
        mController = null;
        selector(Checkable.class).map(new Action<Checkable>() {
            @Override
            public void action(int i, Checkable checkable) {
                checkable.setChecked(false);
            }
        });
        notifyDataSetChanged();
    }*/
}
