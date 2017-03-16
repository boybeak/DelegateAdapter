package com.nulldreams.adapter.annotation;

import com.nulldreams.adapter.DelegateAdapter;
import com.nulldreams.adapter.R;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by gaoyunfei on 2017/2/27.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface OnClick {
    int[] ids () default DelegateAdapter.ITEM_VIEW_ID;
}
