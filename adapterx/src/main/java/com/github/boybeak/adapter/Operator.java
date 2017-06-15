package com.github.boybeak.adapter;

import android.util.Log;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Created by gaoyunfei on 2017/6/15.
 */

public enum Operator {

    OPERATOR_EQUALS{
        @Override
        <V> boolean accept(V v1, V ... v2) {
            return v1 != null && v1.equals(v2[0]);
        }
    },
    OPERATOR_NOT_EQUALS {
        @Override
        <V> boolean accept(V v1, V ... v2) {
            if (v1 == null) {
                return false;
            }
            return !v1.equals(v2[0]);
        }
    },
    OPERATOR_GT {
        @Override
        <V> boolean accept(V v1, V ... v2) {
            if (v1 != null && v2 != null) {
                if (v1 instanceof Comparable) {
                    Comparable c1 = (Comparable)v1;
                    Comparable c2 = (Comparable)v2[0];
                    return c1.compareTo(c2) > 0;
                }
            }
            return false;
        }
    },
    OPERATOR_LT {
        @Override
        <V> boolean accept(V v1, V ... v2) {
            if (v1 != null && v2 != null) {
                if (v1 instanceof Comparable
                        && v1.getClass().isInstance(v2)) {
                    Comparable c1 = (Comparable)v1;
                    Comparable c2 = (Comparable)v2[0];
                    return c1.compareTo(c2) < 0;
                }
            }
            return false;
        }
    },
    OPERATOR_GT_EQUALS {
        @Override
        <V> boolean accept(V v1, V ... v2) {
            if (v1 != null && v2 != null) {
                if (v1 instanceof Comparable
                        && v1.getClass().isInstance(v2)) {
                    Comparable c1 = (Comparable)v1;
                    Comparable c2 = (Comparable)v2[0];
                    return c1.compareTo(c2) >= 0;
                }
            }
            return false;
        }
    },
    OPERATOR_LT_EQUALS {
        @Override
        <V> boolean accept(V v1, V ... v2) {
            if (v1 != null && v2 != null) {
                if (v1 instanceof Comparable
                        && v1.getClass().isInstance(v2)) {
                    Comparable c1 = (Comparable)v1;
                    Comparable c2 = (Comparable)v2[0];
                    return c1.compareTo(c2) <= 0;
                }
            }
            return false;
        }
    },
    OPERATOR_IN {
        @Override
        <V> boolean accept(V v1, V ... v2) {
            List<V> list = Arrays.asList(v2);
            return list.contains(v1);
        }
    },
    OPERATOR_NOT_IN {
        @Override
        <V> boolean accept(V v1, V ... v2) {
            List list = Arrays.asList(v2);
            return !list.contains(v1);
        }
    },
    OPERATOR_BETWEEN {
        @Override
        <V> boolean accept(V v1, V ... v2) {
            if (v1 != null && v2 != null) {
                if (v1 instanceof Comparable) {
                    Arrays.sort(v2);
                    Comparable c1 = (Comparable)v1;
                    return c1.compareTo(v2[0]) > 0
                            && c1.compareTo(v2[v2.length - 1]) < 0;
                }
            }
            return false;
        }
    },
    OPERATOR_I_BETWEEN {
        @Override
        <V> boolean accept(V v1, V ... v2) {
            if (v1 != null && v2 != null) {
                if (v1 instanceof Comparable) {
                    Arrays.sort(v2);
                    Comparable c1 = (Comparable)v1;
                    return c1.compareTo(v2[0]) >= 0
                            && c1.compareTo(v2[v2.length - 1]) < 0;
                }
            }
            return false;
        }
    },
    OPERATOR_BETWEEN_I {
        @Override
        <V> boolean accept(V v1, V ... v2) {
            if (v1 != null && v2 != null) {
                if (v1 instanceof Comparable) {
                    Arrays.sort(v2);
                    Comparable c1 = (Comparable)v1;
                    return c1.compareTo(v2[0]) > 0
                            && c1.compareTo(v2[v2.length - 1]) <= 0;
                }
            }
            return false;
        }
    },
    OPERATOR_I_BETWEEN_I {
        @Override
        <V> boolean accept(V v1, V ... v2) {
            if (v1 != null && v2 != null) {
                if (v1 instanceof Comparable) {
                    Arrays.sort(v2);
                    Comparable c1 = (Comparable)v1;
                    return c1.compareTo(v2[0]) >= 0
                            && c1.compareTo(v2[v2.length - 1]) <= 0;
                }
            }
            return false;
        }
    },
    OPERATOR_IS_NULL {
        @Override
        <V> boolean accept(V v1, V ... v2) {
            return v1 == null;
        }
    };

    <V> boolean accept (V v1, V ... v2) {
        return false;
    }
}
