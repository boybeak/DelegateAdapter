package com.nulldreams.delegateadapter.adapter.event;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.View;

import com.nulldreams.adapter.DelegateAdapter;
import com.nulldreams.adapter.widget.OnItemLongClickListener;
import com.nulldreams.delegateadapter.adapter.delegate.YtbDelegate;
import com.nulldreams.delegateadapter.adapter.holder.YtbHolder;

/**
 * Created by gaoyunfei on 2017/2/27.
 */

public class YtbLongClickListener implements OnItemLongClickListener<YtbDelegate, YtbHolder> {
    @Override
    public boolean onLongClick(View view, Context context, YtbDelegate ytbDelegate, YtbHolder ytbHolder, int position, DelegateAdapter adapter) {
        new AlertDialog.Builder(context)
                .setMessage(ytbDelegate.getSource().getCaption())
                .show();
        return true;
    }
}
