package com.github.boybeak.adapter;

import android.support.annotation.Nullable;
import android.util.Log;

import java.util.ArrayList;
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

    public <V> WhereDelegate<T> where (String key, Operator operator, V ... value) {
        return where(new Where<T, V>(key, operator, value));
    }

    public <V> WhereDelegate<T> where (Where<T, V> where) {

        mWhereDelegate.addWithCheck(where);
        return mWhereDelegate;
    }

    public void map (Action<T> action) {
        mWhereDelegate.map(action);
    }

    public List<T> findAll () {
        return mWhereDelegate.findAll();
    }

    public int count () {
        return mWhereDelegate.count();
    }

    private boolean isT (Object object) {
        return mTClass.isInstance(object);
    }

    public class WhereDelegate<T> {

        private List<Where> whereList = new ArrayList<>();

        public <V> WhereDelegate<T> and (String key, Operator operator, V ... value) {
            return and(new Where<T, V>(Where.AND, key, operator, value));
        }

        public <V> WhereDelegate<T> and (Where<T, V> where) {
            where.setConnector(Where.AND);
            addWithCheck(where);
            return this;
        }

        public <V> WhereDelegate<T> or (String key, Operator operator, V ... value) {
            return or(new Where<T, V>(Where.OR, key, operator, value));
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
            for (Object object : mList) {
                if (isT(object)) {
                    T t = (T)object;
                    if (accept(t)) {
                        tAction.action((T)object);
                    }
                }
            }
        }

        public @Nullable List<T> findAll () {

            if (mList != null && !mList.isEmpty()) {
                List<T> tList = new ArrayList<>();
                for (Object object : mList) {
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

    }

}
