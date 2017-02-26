package com.nulldreams.adapter.annotation;

import android.support.annotation.LayoutRes;
import android.support.v4.os.CancellationSignal;

import com.nulldreams.adapter.AbsViewHolder;
import com.nulldreams.adapter.widget.AbsOnItemClickListener;
import com.nulldreams.adapter.widget.OnItemClickListener;

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
    Class<? extends AbsViewHolder> holderClass ();
    Class<? extends OnItemClickListener> onClick () default AbsOnItemClickListener.class;
}
