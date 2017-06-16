package com.nulldreams.delegateadapter.adapter.event;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.View;

import com.github.boybeak.adapter.DelegateAdapter;
import com.github.boybeak.adapter.widget.OnItemLongClickListener;
import com.nulldreams.delegateadapter.adapter.delegate.TwitterDelegate;
import com.nulldreams.delegateadapter.adapter.holder.TwitterHolder;

/**
 * Created by gaoyunfei on 2017/2/27.
 */

public class TwitterLongClickListener implements OnItemLongClickListener<TwitterDelegate, TwitterHolder> {
    @Override
    public boolean onLongClick(View view, Context context, TwitterDelegate twitterDelegate, TwitterHolder twitterHolder, int position, DelegateAdapter adapter) {
        new AlertDialog.Builder(context)
                .setMessage(twitterDelegate.getSource().getAtName())
                .show();
        return true;
    }
}
