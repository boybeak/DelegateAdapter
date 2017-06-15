package com.github.boybeak.selector;

import android.support.annotation.IntDef;
import android.text.TextUtils;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Field;

/**
 * Created by gaoyunfei on 2017/6/14.
 */

public class Where<T, V> {

    private static final String TAG = Where.class.getSimpleName();

    static final int NONE = 0, AND = 1, OR = 2, XOR = 3;

    /*public static final String  = "<=",  = "in",
             = "not in",  = "between",  = "is null",
            OPERATOR_IS = "is", OPERATOR_IS_NOT = "is not";*/

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({NONE, AND, OR, XOR})
    public @interface Connector{}

    /*@Retention(RetentionPolicy.SOURCE)
    @StringDef({
            OPERATOR_EQUALS, OPERATOR_NOT_EQUALS, OPERATOR_GT,
            OPERATOR_LT, OPERATOR_GT_EQUALS, OPERATOR_LT_EQUALS,
            OPERATOR_IN, OPERATOR_NOT_IN, OPERATOR_BETWEEN,
            OPERATOR_IS_NULL, OPERATOR_IS, OPERATOR_IS_NOT
    })
    public @interface Operator{}*/

    private String key;
    private Operator operator;
    private V[] value;

    private @Connector int connector = NONE;

    public Where (String key, Operator operator, V ... value) {
        this (NONE, key, operator, value);
    }

    Where (@Connector int connector, String key, Operator operator, V ... value) {
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

    public String getKey() {
        return key;
    }

    public V[] getValue() {
        return value;
    }

    boolean accept (T t) {
        try {
            Object obj = getKey(t);
            return operator.accept(obj, value);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return false;
    }

    private Object getKey (T t) throws NoSuchFieldException {
        String[] path = key.split("\\.");
        if (path.length == 0) {
            return t;
        }
        Object obj = t;
        for (String fieldName : path) {
            if (TextUtils.isEmpty(fieldName)) {
                continue;
            }
            Field field = obj.getClass().getDeclaredField(fieldName);
            try {
                obj = field.get(obj);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        return obj;
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
