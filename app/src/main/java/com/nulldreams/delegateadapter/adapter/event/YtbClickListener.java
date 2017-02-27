package com.nulldreams.delegateadapter.adapter.event;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

import com.nulldreams.adapter.DelegateAdapter;
import com.nulldreams.adapter.widget.OnItemClickListener;
import com.nulldreams.delegateadapter.adapter.delegate.YtbDelegate;
import com.nulldreams.delegateadapter.adapter.holder.YtbHolder;

/**
 * Created by gaoyunfei on 2017/2/27.
 */

public class YtbClickListener implements OnItemClickListener<YtbDelegate,YtbHolder> {
    @Override
    public void onClick(View view, Context context, YtbDelegate ytbDelegate, YtbHolder ytbHolder, int position, DelegateAdapter adapter) {
        Toast.makeText(context, YtbHolder.class.getSimpleName(), Toast.LENGTH_SHORT).show();
    }
}
