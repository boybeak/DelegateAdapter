package com.github.boybeak.selector;

import android.support.annotation.Nullable;
import android.util.Log;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by gaoyunfei on 2017/6/14.
 */

public class Selector<T> {

    private static final String TAG = Selector.class.getSimpleName();

    public static <T> Selector<T> selector (Class<T> tClass, List list) {
        return new Selector<>(tClass, list);
    }

    private Class<T> mTClass;
    private List mList;

    private WhereDelegate<T> mWhereDelegate;

    Selector (Class<T> tClass, List list) {
        mTClass = tClass;
        mList = list;

        mWhereDelegate = new WhereDelegate<T>();
    }

    public <V> WhereDelegate<T> where (Path<T, V> path, Operator operator, V ... value) {
        return where(new Where<T, V>(path, operator, value));
    }

    public <V> WhereDelegate<T> where (Where<T, V> where) {

        mWhereDelegate.addWithCheck(where);
        return mWhereDelegate;
    }

    public void map (Action<T> action) {
        mWhereDelegate.map(action);
    }

    public @Nullable List<T> findAll () {
        return mWhereDelegate.findAll();
    }

    public @Nullable T findFirst () {
        return mWhereDelegate.findFirst();
    }

    public @Nullable T findLast () {
        return mWhereDelegate.findLast();
    }

    public int count () {
        return mWhereDelegate.count();
    }

    public @Nullable <V> List<V> extractAll(Path<T, V> path) {
        return mWhereDelegate.extractAll(path);
    }

    public @Nullable <V> V extractFirst (final Path<T, V> path) {
        return mWhereDelegate.extractFirst(path);
    }

    public @Nullable <V> V extractLast (final Path<T, V> path) {
        return mWhereDelegate.extractLast(path);
    }

    private boolean isT (Object object) {
        return mTClass.isInstance(object);
    }


    /**
     *
     * @param <T>
     */
    public class WhereDelegate<T> {

        private List<Where> whereList = new ArrayList<>();

        public <V> WhereDelegate<T> and (Path<T, V> path, Operator operator, V ... value) {
            return and(new Where<T, V>(Where.AND, path, operator, value));
        }

        public <V> WhereDelegate<T> and (Where<T, V> where) {
            where.setConnector(Where.AND);
            addWithCheck(where);
            return this;
        }

        public <V> WhereDelegate<T> or (Path<T, V> path, Operator operator, V ... value) {
            return or(new Where<T, V>(Where.OR, path, operator, value));
        }

        public <V> WhereDelegate<T> or (Where<T, V> where) {
            where.setConnector(Where.OR);
            addWithCheck (where);
            return this;
        }

        <V> void addWithCheck (Where<T, V> where) {
            if (!whereList.contains(where)) {
                whereList.add(where);
            }
        }

        boolean acceptAnd (boolean a, boolean b) {
            return a && b;
        }

        boolean acceptOr (boolean a, boolean b) {
            return a || b;
        }

        boolean acceptXor (boolean a, boolean b) {
            return a == b;
        }

        <V> boolean accept (T t) {
            if (whereList.isEmpty()) {
                return true;
            }
            final int length = whereList.size();
            boolean result = whereList.get(0).accept(t);
//            Log.v(TAG, "accept before loop=" + result);
            for (int i = 1; i < length; i++) {
                Where<T, V> where = whereList.get(i);
//                Log.v(TAG, "accept " + where.getConnector() + " " + where.getKey());
                switch (where.getConnector()) {
                    case Where.AND:
                        result = acceptAnd(result, where.accept(t));
                        break;
                    case Where.OR:
                        result = acceptOr(result, where.accept(t));
                        break;
                    case Where.XOR:
                        result = acceptXor(result, where.accept(t));
                        break;
                    case Where.NONE:
                        continue;
                }
//                Log.v(TAG, "accept result=" + result + " after=" + where.getKey());
            }
            return result;
        }

        public void map (Action<T> tAction) {
            if (mList != null && !mList.isEmpty()) {
                final int size = mList.size();
                for (int i = 0; i < size; i++) {
                    Object object = mList.get(i);
                    if (isT(object)) {
                        T t = (T)object;
                        if (accept(t)) {
                            tAction.action(i, (T)object);
                        }
                    }
                }
            }
        }

        public @Nullable List<T> findAll () {
            List<T> tList = new ArrayList<>();
            if (mList != null && !mList.isEmpty()) {

                int size = mList.size();
                for (int i = 0; i < size; i++) {
                    Object object = mList.get(i);
                    if (isT(object)) {
                        T t = (T)object;
                        if (whereList.isEmpty() || accept(t)) {
                            tList.add(t);
                        }
                    }
                }
                return tList;
            }
            return null;
        }

        public @Nullable T findFirst () {
            if (mList != null && !mList.isEmpty()) {
                int size = mList.size();
                for (int i = 0; i < size; i++) {
                    Object object = mList.get(i);
                    if (isT(object)) {
                        T t = (T)object;
                        if (whereList.isEmpty() || accept(t)) {
                            return t;
                        }
                    }
                }
            }
            return null;
        }

        public @Nullable T findLast () {
            if (mList != null && !mList.isEmpty()) {
                int size = mList.size();
                for (int i = size - 1; i >= 0; i--) {
                    Object object = mList.get(i);
                    if (isT(object)) {
                        T t = (T)object;
                        if (whereList.isEmpty() || accept(t)) {
                            return t;
                        }
                    }
                }
            }
            return null;
        }

        public int count () {
            if (mList != null && !mList.isEmpty()) {
                int count = 0;
                for (Object object : mList) {

                    if (isT(object)) {
                        T t = (T)object;
                        if (whereList.isEmpty() || accept(t)) {
                            count++;
                        }
                    }
                }
                return count;
            }
            return 0;
        }

        public @Nullable <V> List<V> extractAll (final Path<T, V> path) {
            return extractAll(path, false);
        }

        public @Nullable <V> List<V> extractAll (final Path<T, V> path, final boolean ignoreRepeat) {

            if (mList != null && !mList.isEmpty()) {
                List<V> vList = new ArrayList<>();
                final int size = mList.size();
                for (int i = 0; i < size; i++) {
                    Object object = mList.get(i);
                    if (isT(object)) {
                        T t = (T)object;
                        if (accept(t)) {
                            V v = path.extract(t);
                            if (ignoreRepeat && vList.contains(v)) {
                                continue;
                            }
                            vList.add(v);
                        }
                    }
                }
                return vList;
            }
            return null;

        }

        public @Nullable <V> V extractFirst (final Path<T, V> path) {

            if (mList != null && !mList.isEmpty()) {
                final int size = mList.size();
                for (int i = 0; i < size; i++) {
                    Object object = mList.get(i);
                    if (isT(object)) {
                        T t = (T)object;
                        if (accept(t)) {
                            return path.extract(t);
                        }
                    }
                }
            }
            return null;

        }

        public @Nullable <V> V extractLast (final Path<T, V> path) {

            if (mList != null && !mList.isEmpty()) {
                final int size = mList.size();
                for (int i = size - 1; i >= 0; i--) {
                    Object object = mList.get(i);
                    if (isT(object)) {
                        T t = (T)object;
                        if (accept(t)) {
                            return path.extract(t);
                        }
                    }
                }
            }
            return null;

        }

        public void remove () {
            if (mList != null && !mList.isEmpty()) {
                Iterator iterator = mList.iterator();
                while (iterator.hasNext()) {
                    Object obj = iterator.next();
                    if (isT(obj)) {
                        T t = (T)obj;
                        if (whereList.isEmpty() || accept(t)) {
                            iterator.remove();
                        }
                    }
                }
            }
        }

    }

}
