package com.nulldreams.delegateadapter.adapter.event;

import android.content.Context;
import android.util.Log;
import android.view.View;

import com.nulldreams.adapter.DelegateAdapter;
import com.nulldreams.adapter.widget.OnItemClickListener;
import com.nulldreams.delegateadapter.adapter.TextDelegate;
import com.nulldreams.delegateadapter.adapter.TextHolder;

/**
 * Created by gaoyunfei on 2017/3/16.
 */

public class TextClickListener implements OnItemClickListener<TextDelegate, TextHolder> {

    private static final String TAG = TextClickListener.class.getSimpleName();

    @Override
    public void onClick(View view, Context context, TextDelegate textDelegate, TextHolder textHolder, int position, DelegateAdapter adapter) {
        Log.v(TAG, "onClick " + view.getId());
    }
}
