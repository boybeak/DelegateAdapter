package com.github.boybeak.adapter.extension.touch;

import android.content.Context;

import com.github.boybeak.adapter.DelegateAdapter;

import java.util.Collections;

/**
 * Created by gaoyunfei on 2017/7/6.
 */

public class TouchableAdapter extends DelegateAdapter implements Touchable {

    public TouchableAdapter(Context context) {
        super(context);
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(getList(), i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(getList(), i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    @Override
    public void onItemDismiss(int position) {
        remove(position).autoNotify();
    }
}
