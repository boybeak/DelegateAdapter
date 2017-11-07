package com.github.boybeak.adapter;

import android.os.Bundle;
import android.view.View;

public interface OnViewEventListener<Data, AVH> {
    void onViewEvent (int eventCode, View view, Data data, Bundle bundle, AVH viewHolder,
                      int position, DelegateAdapter adapter);
}