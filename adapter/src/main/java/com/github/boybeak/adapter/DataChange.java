package com.github.boybeak.adapter;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by gaoyunfei on 2017/4/27.
 */

public final class DataChange {

    public static final int
            TYPE_ITEM_JUST_NOTIFY = 0,
            TYPE_ITEM_INSERTED = 1,
            TYPE_ITEM_RANGE_INSERTED = 2,
            TYPE_ITEM_CHANGED = 3,
            TYPE_ITEM_RANGE_CHANGED = 4,
            TYPE_ITEM_REMOVED = 5,
            TYPE_ITEM_RANGE_REMOVED = 6,
            TYPE_ITEM_MOVED = 7;

    public static DataChange doNothingInstance () {
        return new DataChange(true);
    }

    public static DataChange notifyDataSetChangeInstance (DelegateAdapter adapter) {
        return new DataChange(adapter, 0, TYPE_ITEM_JUST_NOTIFY);
    }

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({
            TYPE_ITEM_JUST_NOTIFY,
            TYPE_ITEM_INSERTED,
            TYPE_ITEM_RANGE_INSERTED,
            TYPE_ITEM_CHANGED,
            TYPE_ITEM_RANGE_CHANGED,
            TYPE_ITEM_REMOVED,
            TYPE_ITEM_RANGE_REMOVED,
            TYPE_ITEM_MOVED
    })
    public @interface Type{}

    private DelegateAdapter mAdapter;
    private int fromPosition, itemCountOrToPosition;
    private @DataChange.Type int type;
    private boolean doNothing = false;

    /**
     * @param adapter
     * @param from
     * @param countOrToPosition if {@link DataChange#type} is {@link DataChange#TYPE_ITEM_MOVED},
     *                          this params is toPosition, else is itemCount. And this will not work for
     *                          {@link DataChange#TYPE_ITEM_INSERTED},{@link DataChange#TYPE_ITEM_CHANGED},
     *                          and {@link DataChange#TYPE_ITEM_REMOVED}
     * @param type
     */
    public DataChange (DelegateAdapter adapter, int from, int countOrToPosition, @Type int type) {
        mAdapter = adapter;
        this.fromPosition = from;
        this.itemCountOrToPosition = countOrToPosition;
        this.type = type;
    }

    public DataChange(DelegateAdapter adapter, int from, @Type int type) {
        this(adapter, from, 0, type);
    }

    private DataChange (boolean doNothing) {
        this.doNothing = doNothing;
    }

    public DelegateAdapter autoNotify () {
        return autoNotify(null);
    }

    public DelegateAdapter autoNotify (Object payLoad) {
        if (doNothing) {
            return mAdapter;
        }
        switch (type) {
            case TYPE_ITEM_INSERTED:
                mAdapter.notifyItemInserted(fromPosition);
                break;
            case TYPE_ITEM_RANGE_INSERTED:
                mAdapter.notifyItemRangeInserted(fromPosition, itemCountOrToPosition);
                break;
            case TYPE_ITEM_CHANGED:
                if (payLoad == null) {
                    mAdapter.notifyItemChanged(fromPosition);
                } else {
                    mAdapter.notifyItemChanged(fromPosition, payLoad);
                }
                break;
            case TYPE_ITEM_RANGE_CHANGED:
                if (payLoad == null) {
                    mAdapter.notifyItemRangeChanged(fromPosition, itemCountOrToPosition);
                } else {
                    mAdapter.notifyItemRangeChanged(fromPosition, itemCountOrToPosition, payLoad);
                }
                break;
            case TYPE_ITEM_REMOVED:
                mAdapter.notifyItemRemoved(fromPosition);
                break;
            case TYPE_ITEM_RANGE_REMOVED:
                mAdapter.notifyItemRangeRemoved(fromPosition, itemCountOrToPosition);
                break;
            case TYPE_ITEM_MOVED:
                mAdapter.notifyItemMoved(fromPosition, itemCountOrToPosition);
                break;
            default:
                mAdapter.notifyDataSetChanged();
                break;
        }
        return mAdapter;
    }

    public DelegateAdapter notifyDataSetChanged () {
        mAdapter.notifyDataSetChanged();
        return mAdapter;
    }

    public DelegateAdapter dontNotifyChanged () {
        return mAdapter;
    }
}
