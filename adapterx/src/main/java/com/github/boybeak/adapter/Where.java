package com.github.boybeak.adapter;

import android.support.annotation.IntDef;
import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by gaoyunfei on 2017/6/14.
 */

public class Where<T> {

    static final int NONE = 0, AND = 1, OR = 2, XOR = 3;

    public static final String OPERATOR_EQUALS = "equals",
            OPERATOR_NOT_EQUALS = "not equals", OPERATOR_GT = ">", OPERATOR_LT = "<",
            OPERATOR_GT_EQUALS = ">=", OPERATOR_LT_EQUALS = "<=", OPERATOR_IN = "in",
            OPERATOR_NOT_IN = "not in", OPERATOR_BETWEEN = "between", OPERATOR_IS_NULL = "is null",
            OPERATOR_IS = "is", OPERATOR_IS_NOT = "is not";

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({NONE, AND, OR, XOR})
    public @interface Connector{}

    @Retention(RetentionPolicy.SOURCE)
    @StringDef({
            OPERATOR_EQUALS, OPERATOR_NOT_EQUALS, OPERATOR_GT,
            OPERATOR_LT, OPERATOR_GT_EQUALS, OPERATOR_LT_EQUALS,
            OPERATOR_IN, OPERATOR_NOT_IN, OPERATOR_BETWEEN,
            OPERATOR_IS_NULL, OPERATOR_IS, OPERATOR_IS_NOT
    })
    public @interface Operator{}

    private String key, value;
    private @Operator String operator;

    private @Connector int connector = NONE;

    public Where (String key, @Operator String operator, String value) {
        this (key, operator, value, NONE);
    }

    Where (String key, @Operator String operator, String value, @Connector int connector) {
        this.key = key;
        this.operator = operator;
        this.value = value;
        this.connector = connector;
    }

    public int getConnector() {
        return connector;
    }

    void setConnector(int connector) {
        this.connector = connector;
    }

    boolean accept (T t) {
        switch (operator) {
            case OPERATOR_EQUALS:
                break;
            case OPERATOR_NOT_EQUALS:
                break;
            case OPERATOR_GT:
                break;
            case OPERATOR_LT:
                break;
            case OPERATOR_GT_EQUALS:
                break;
            case OPERATOR_LT_EQUALS:
                break;
            case OPERATOR_IN:
                break;
            case OPERATOR_NOT_IN:
                break;
            case OPERATOR_BETWEEN:
                break;
            case OPERATOR_IS_NULL:
                break;
            case OPERATOR_IS:
                break;
            case OPERATOR_IS_NOT:
                break;
        }
        return false;
    }

    private Object getKey (T t) throws NoSuchFieldException {
        String[] path = key.split(".");
        if (path.length == 0) {
            return t;
        }
        Class clz = t.getClass();
        Object obj = null;
        for (String fieldName : path) {
            Field field = clz.getDeclaredField(fieldName);
            try {
                Method method = clz.getDeclaredMethod(fieldName);
                method.invoke()
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
            if (clz.getDeclaredField())
        }
        //t.getClass().getSimpleName()
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null && obj instanceof Where) {
            Where ow = (Where)obj;
            return key != null && key.equals(ow.key)
                && operator != null && operator.equals(ow.operator)
                && value != null && value.equals(ow.value);
        }
        return false;
    }
}
