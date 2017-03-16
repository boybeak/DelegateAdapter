package com.nulldreams.delegateadapter.adapter.event;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.nulldreams.adapter.DelegateAdapter;
import com.nulldreams.adapter.widget.OnItemClickListener;
import com.nulldreams.delegateadapter.R;
import com.nulldreams.delegateadapter.adapter.delegate.YtbDelegate;
import com.nulldreams.delegateadapter.adapter.holder.YtbHolder;

/**
 * Created by gaoyunfei on 2017/2/27.
 */

public class YtbClickListener implements OnItemClickListener<YtbDelegate,YtbHolder> {

    private static final String TAG = YtbClickListener.class.getSimpleName();

    @Override
    public void onClick(View view, Context context, YtbDelegate ytbDelegate, YtbHolder ytbHolder, int position, DelegateAdapter adapter) {
        Log.v(TAG, "onClick " + view.getId());
        switch (view.getId()) {
            case R.id.thumb:
                Toast.makeText(context, "thumb " + YtbHolder.class.getSimpleName(), Toast.LENGTH_SHORT).show();
                break;
            default:
                Toast.makeText(context, YtbHolder.class.getSimpleName(), Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
