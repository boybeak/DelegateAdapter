package com.nulldreams.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.nulldreams.adapter.impl.DelegateImpl;

/**
 * Created by gaoyunfei on 16/7/29.
 * An abstract class for all ViewHolder, generally T is a subclass of {@link DelegateImpl}
 */
public abstract class AbsViewHolder<T> extends RecyclerView.ViewHolder {

    public AbsViewHolder(View itemView) {
        super(itemView);
    }

    /**
     * @param context context
     * @param t may be a subclass of {@link DelegateImpl}
     * @param position adapter position
     * @param adapter adapter
     */
    public abstract void onBindView (Context context, T t, int position, DelegateAdapter adapter);

    public void onViewAttachedToWindow (Context context) {

    }

    public void onViewDetachedFromWindow (Context context) {
    }

    public final View findViewById (int id) {
        return itemView.findViewById(id);
    }

    public final View findViewByTag (Object tag) {
        return itemView.findViewWithTag(tag);
    }
}
