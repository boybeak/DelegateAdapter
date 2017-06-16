package com.github.boybeak.adapter.annotation;

import com.github.boybeak.adapter.DelegateAdapter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by gaoyunfei on 2017/2/27.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface OnLongClick {
    int[] ids () default DelegateAdapter.ITEM_VIEW_ID;
}
