package com.github.boybeak.selector;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by gaoyunfei on 2017/6/21.
 */

public abstract class Selector<T> {

    public static <T> ListSelector<T> selector (@NonNull Class<T> tClass, @NonNull List list) {
        return new ListSelector<>(tClass, list);
    }

    public static <T> ArraySelector<T> selector (@NonNull Class<T> tClass, @NonNull Object[] ts) {
        return new ArraySelector<>(tClass, ts);
    }

    private Class<T> mTClass;

    Selector (Class<T> tClass) {
        mTClass = tClass;
    }

    public abstract int getSize ();
    public abstract Object get (int index);
    abstract WhereDelegate<T> getWhereDelegate ();
    public boolean isEmpty () {
        return getSize() == 0;
    }

    public @NonNull Class<T> getTargetClass () {
        return mTClass;
    }

    public <V> WhereDelegate<T> where (Path<T, V> path, Operator operator, V ... value) {
        return where(new Where<T, V>(path, operator, value));
    }

    public <V> WhereDelegate<T> where (Where<T, V> where) {
        getWhereDelegate().addWithCheck(where);
        return getWhereDelegate();
    }

    public void map (Action<T> action) {
        getWhereDelegate().map(action);
    }

    public @Nullable List<T> findAll () {
        return getWhereDelegate().findAll();
    }

    public @Nullable T findFirst () {
        return getWhereDelegate().findFirst();
    }

    public @Nullable T findLast () {
        return getWhereDelegate().findLast();
    }

    public int count () {
        return getWhereDelegate().count();
    }

    public @Nullable <V> List<V> extractAll(Path<T, V> path) {
        return getWhereDelegate().extractAll(path);
    }

    public @Nullable <V> V extractFirst (final Path<T, V> path) {
        return getWhereDelegate().extractFirst(path);
    }

    public @Nullable <V> V extractLast (final Path<T, V> path) {
        return getWhereDelegate().extractLast(path);
    }

}
