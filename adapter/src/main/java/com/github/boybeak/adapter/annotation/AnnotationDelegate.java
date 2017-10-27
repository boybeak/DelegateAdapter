package com.github.boybeak.adapter.annotation;

import android.os.Bundle;

import com.github.boybeak.adapter.AbsDelegate;
import com.github.boybeak.adapter.AbsViewHolder;
import com.github.boybeak.adapter.DelegateAdapter;

import static com.github.boybeak.adapter.DelegateAdapter.getLayoutFromAnnotation;

/**
 *
 * In a subclass of this, you must provide {@link DelegateInfo} for fields with annotation
 * {@link LayoutID} and {@link HolderClass}.
 * In library project, you can not use {@link DelegateInfo}
 * For example, a subclass as below:
 * {@link DelegateInfo()}
 * public class DemoClass extends AnnotationDelegate<String> {
 *      \@{@link LayoutID}
 *      public int layoutId = R.layout.xxx;
 *      \@{@link HolderClass}
 *      public Class<XXXViewHolder> holderClass;
 * }
 * Created by gaoyunfei on 2016/12/17.
 */

public abstract class AnnotationDelegate<T, AVH extends AbsViewHolder> extends AbsDelegate<T, AVH> {

    private static final String TAG = AnnotationDelegate.class.getSimpleName();

    private int layoutID = 0;
    private Class<AVH> holderClass;

    public AnnotationDelegate(T t) {
        super(t);
    }

    public AnnotationDelegate(T t, Bundle bundle) {
        super(t, bundle);
    }

    public AnnotationDelegate(T t, OnViewEventListener<T, AVH> onViewEventListener) {
        super(t, onViewEventListener);
    }

    public AnnotationDelegate(T t, Bundle bundle, OnViewEventListener<T, AVH> onViewEventListener) {
        super(t, bundle, onViewEventListener);
    }

    /**
     * find layout res id in {@link DelegateInfo}, if not found, then with {@link LayoutID}
     * @return
     */
    @Override
    public int getLayout() {
        if (layoutID > 0) {
            return layoutID;
        }

        layoutID = getLayoutFromAnnotation(this);
        return layoutID;
    }

    /**
     * find holder class in {@link DelegateInfo} first, if not found, then with {@link HolderClass}
     * @return
     */
    public Class<AVH> getHolderClass () {
        if (holderClass != null) {
            return holderClass;
        }
        holderClass = DelegateAdapter.getHolderClassFromAnnotation(this);
        return holderClass;
    }
}
