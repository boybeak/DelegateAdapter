package com.github.boybeak.selector;

import android.support.annotation.NonNull;

/**
 * Created by gaoyunfei on 2017/6/21.
 */

public class ArraySelector<T> extends Selector<T> {

    private Object[] mTs;
    private WhereDelegate<T> mWhereDelegate;

    public ArraySelector (@NonNull Class<T> tClass, @NonNull Object[] ts) {
        super(tClass);
        mTs = ts;
        mWhereDelegate = new WhereDelegate<>(this);
    }

    @Override
    public int getSize() {
        return mTs.length;
    }

    @Override
    public Object get(int index) {
        return mTs[index];
    }

    @Override
    WhereDelegate<T> getWhereDelegate() {
        return mWhereDelegate;
    }
}
