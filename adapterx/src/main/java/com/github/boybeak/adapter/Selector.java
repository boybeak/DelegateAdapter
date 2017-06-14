package com.github.boybeak.adapter;

import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gaoyunfei on 2017/6/14.
 */

public class Selector<T> {

    private Class<T> mTClass;
    private Order mOrder;

    private WhereDelegate<T> mWhereDelegate;

    Selector (Class<T> tClass, Order order) {
        mTClass = tClass;
        mOrder = order;

        mWhereDelegate = new WhereDelegate<T>();
    }

    public WhereDelegate<T> where (String key, String operator, String value) {
        return where(new Where<T>(key, operator, value));
    }

    public WhereDelegate<T> where (Where<T> where) {

        mWhereDelegate.addWithCheck(where);
        return mWhereDelegate;
    }

    public void map (Action<T> action) {
        mWhereDelegate.map(action);
    }

    public List<T> findAll () {
        return mWhereDelegate.findAll();
    }

    private boolean isT (Object object) {
        return mTClass.isInstance(object);
    }

    public class WhereDelegate<T> {

        private List<Where> whereList = new ArrayList<>();

        public WhereDelegate<T> and (String key, String operator, String value) {
            return and(new Where<T>(key, operator, value, Where.AND));
        }

        public WhereDelegate<T> and (Where<T> where) {
            where.setConnector(Where.AND);
            addWithCheck(where);
            return this;
        }

        public WhereDelegate<T> or (String key, String operator, String value) {
            return or(new Where<T>(key, operator, value, Where.OR));
        }

        public WhereDelegate<T> or (Where<T> where) {
            where.setConnector(Where.OR);
            addWithCheck (where);
            return this;
        }

        void addWithCheck (Where<T> where) {
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

        boolean accept (T t) {
            if (whereList.isEmpty()) {
                return true;
            }
            final int length = whereList.size();
            boolean result = whereList.get(0).accept(t);
            for (int i = 1; i < length; i++) {
                Where<T> where = whereList.get(i);
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
            }
            return result;
        }

        public void map (Action<T> tAction) {
            for (Object object : mOrder.getDataList()) {
                if (isT(object)) {
                    T t = (T)object;
                    if (accept(t)) {
                        tAction.action((T)object);
                    }
                }
            }
        }

        public @Nullable List<T> findAll () {

            List dataList = mOrder.getDataList();
            if (dataList != null && !dataList.isEmpty()) {
                List<T> tList = new ArrayList<>();
                for (Object object : dataList) {

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

    }

}
