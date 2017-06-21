package com.github.boybeak.selector;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by gaoyunfei on 2017/6/14.
 */

public class ListSelector<T> extends Selector<T>{

    private List mList;

    private ListWhereDelegate<T> mWhereDelegate;

    ListSelector(@NonNull Class<T> tClass, @NonNull List list) {
        super(tClass);
        mList = list;

        mWhereDelegate = new ListWhereDelegate<T>(this);
    }

    @Override
    public <V> ListWhereDelegate<T> where(Path<T, V> path, Operator operator, V... value) {
        return where(new Where<T, V>(path, operator, value));
    }

    @Override
    public <V> ListWhereDelegate<T> where(Where<T, V> where) {
        getWhereDelegate().addWithCheck(where);
        return getWhereDelegate();
    }

    @Override
    public int getSize() {
        return mList.size();
    }

    @Override
    public Object get(int index) {
        return mList.get(index);
    }

    @Override
    ListWhereDelegate<T> getWhereDelegate() {
        return mWhereDelegate;
    }

    public class ListWhereDelegate<T> extends WhereDelegate<T> {

        ListWhereDelegate(Selector<T> selector) {
            super(selector);
        }

        public void remove () {
            if (!isEmpty()) {
                Iterator iterator = mList.iterator();
                while (iterator.hasNext()) {
                    Object obj = iterator.next();
                    if (isT(obj)) {
                        T t = (T)obj;
                        if (getWhereList().isEmpty() || accept(t)) {
                            iterator.remove();
                        }
                    }
                }
            }
        }
    }
}
