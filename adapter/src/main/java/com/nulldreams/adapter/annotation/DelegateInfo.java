package com.nulldreams.adapter.annotation;

import android.support.annotation.LayoutRes;

import com.nulldreams.adapter.AbsViewHolder;
import com.nulldreams.adapter.DelegateAdapter;
import com.nulldreams.adapter.widget.OnItemClickListener;
import com.nulldreams.adapter.widget.OnItemLongClickListener;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by gaoyunfei on 2016/12/17.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface DelegateInfo {
    @LayoutRes int layoutID () default 0;
    Class<? extends AbsViewHolder> holderClass () default NullHolder.class;
    Class<? extends OnItemClickListener> onClick () default NullOnItemClickListener.class;
    int[] onClickIds() default DelegateAdapter.ITEM_VIEW_ID;
    Class<? extends OnItemLongClickListener> onLongClick () default NullOnItemLongClickListener.class;
    int[] onLongClickIds() default DelegateAdapter.ITEM_VIEW_ID;
}
