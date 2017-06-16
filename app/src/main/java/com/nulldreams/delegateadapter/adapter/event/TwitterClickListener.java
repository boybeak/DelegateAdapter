package com.nulldreams.delegateadapter.adapter.event;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

import com.github.boybeak.adapter.DelegateAdapter;
import com.github.boybeak.adapter.widget.OnItemClickListener;
import com.nulldreams.delegateadapter.adapter.delegate.TwitterDelegate;
import com.nulldreams.delegateadapter.adapter.holder.TwitterHolder;

/**
 * Created by gaoyunfei on 2017/2/27.
 */

public class TwitterClickListener implements OnItemClickListener<TwitterDelegate, TwitterHolder> {
    @Override
    public void onClick(View view, Context context, TwitterDelegate twitterDelegate, TwitterHolder twitterHolder, int position, DelegateAdapter adapter) {
        Toast.makeText(context, TwitterHolder.class.getSimpleName(), Toast.LENGTH_SHORT).show();
    }
}
