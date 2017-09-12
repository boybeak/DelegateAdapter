package com.github.boybeak.adapter.extention.touch;

/**
 * Created by gaoyunfei on 2017/7/6.
 */

interface Touchable {
    boolean onItemMove(int fromPosition, int toPosition);
    void onItemDismiss(int position);
}
