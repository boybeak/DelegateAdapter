package com.nulldreams.adapter.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by gaoyunfei on 2016/12/18.<br/>
 * A advanced subclass of {@link DelegateAdapter}, use this with {@link AnnotationDelegate} is much more effective
 */

public class AnnotationAdapter extends DelegateAdapter {

    private static final String TAG = AnnotationAdapter.class.getSimpleName();

    private Map<Integer, Class<? extends AbsViewHolder>> mTypeHolderMap = new HashMap<>();

    public AnnotationAdapter(Context context) {
        super(context);
    }

    @Override
    public final AbsViewHolder onCreateAbsViewHolder(ViewGroup parent, int viewType, View itemView) {
        if (canFindHolderClass(viewType)) {
            return getHolder(viewType, itemView);
        }
        return onHolderNotFound(parent, viewType, itemView);
    }

    /**
     * if can not find {@link AbsViewHolder} class in {@link #mTypeHolderMap}, provide one in this method
     * @param parent
     * @param viewType
     * @param itemView
     * @return
     */
    public AbsViewHolder onHolderNotFound (ViewGroup parent, int viewType, View itemView) {
        throw new IllegalStateException("can not find a ViewHolder, you can provide on in this method, you must override this method, remove super.onHolderNotFound");
    }

    /**
     * store unknown viewType and AbsViewHolder class to {@link #mTypeHolderMap}
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position) {
        DelegateImpl impl = getList().get(position);
        final int type = impl.getType();
        if (impl instanceof AnnotationDelegate) {
            AnnotationDelegate delegate = (AnnotationDelegate)impl;
            if (!canFindHolderClass(type)) {
                Class<? extends AbsViewHolder> clz = delegate.getHolderClass();
                if (clz != null) {
                    mTypeHolderMap.put(type, clz);
                }
            }
        }
        return type;
    }

    /**
     * make an {@link AbsViewHolder} instance for {@param viewType} with {@param itemView}
     * @param viewType
     * @param itemView
     * @return
     */
    private AbsViewHolder getHolder (int viewType, View itemView) {
        Class<? extends AbsViewHolder> clz = mTypeHolderMap.get(viewType);
        try {
            Constructor<? extends AbsViewHolder> constructor = clz.getConstructor(View.class);
            return constructor.newInstance(itemView);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param viewType
     * @return true if mTypeHolderMap can provide a {@link AbsViewHolder} class for {@param viewType}
     */
    private boolean canFindHolderClass (int viewType) {
        return mTypeHolderMap.containsKey(viewType) && mTypeHolderMap.get(viewType) != null;
    }
}
