package com.nulldreams.delegateadapter.adapter.event;

import android.content.Context;
import android.os.UserHandle;
import android.view.View;
import android.widget.Toast;

import com.nulldreams.adapter.DelegateAdapter;
import com.nulldreams.adapter.widget.OnItemClickListener;
import com.nulldreams.delegateadapter.adapter.UserDelegate;
import com.nulldreams.delegateadapter.adapter.UserHolder;

/**
 * Created by gaoyunfei on 2017/2/28.
 */

public class UserClickListener implements OnItemClickListener<UserDelegate, UserHolder> {
    @Override
    public void onClick(View view, Context context, UserDelegate userDelegate, UserHolder userHolder, int position, DelegateAdapter adapter) {
        Toast.makeText(context, UserHolder.class.getSimpleName(), Toast.LENGTH_SHORT).show();
    }
}
