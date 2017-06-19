package com.nulldreams.delegateadapter.adapter.event;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.github.boybeak.selector.Operator;
import com.github.boybeak.adapter.DelegateAdapter;
import com.github.boybeak.adapter.widget.OnItemClickListener;
import com.github.boybeak.selector.Path;
import com.nulldreams.delegateadapter.adapter.UserDelegate;
import com.nulldreams.delegateadapter.adapter.UserHolder;

import java.util.List;

/**
 * Created by gaoyunfei on 2017/2/28.
 */

public class UserClickListener implements OnItemClickListener<UserDelegate, UserHolder> {

    private static final String TAG = UserClickListener.class.getSimpleName();

    @Override
    public void onClick(View view, Context context, UserDelegate userDelegate, UserHolder userHolder, int position, final DelegateAdapter adapter) {
        int count = adapter.selector(UserDelegate.class)
                .where(Path.with(UserDelegate.class, Integer.class).methodWith("getSource").methodWith("getName").methodWith("length"), Operator.OPERATOR_GT, 4)
                .count();
        /*adapter.selector(UserDelegate.class).where("getSource.getName.length", Operator.OPERATOR_GT, 4).map(new Action<UserDelegate>() {
            @Override
            public void action(int index, UserDelegate userDelegate) {
                StringBuilder sb = new StringBuilder(userDelegate.getSource().getName());
                userDelegate.getSource().setName(sb.deleteCharAt(sb.length() - 1).toString());
                adapter.notifyItemChanged(index);
            }
        });*/
        List<String> names = adapter.selector(UserDelegate.class)
                .where(Path.with(UserDelegate.class, Integer.class).methodWith("getSource").methodWith("getName").methodWith("length"), Operator.OPERATOR_GT, 4)
                .extractAll(Path.with(UserDelegate.class, String.class).methodWith("getSource").methodWith("getName"));
        for (String name : names) {
            Log.v(TAG, "name is " + name);
        }
        Toast.makeText(context, UserHolder.class.getSimpleName() + " count=" + count, Toast.LENGTH_SHORT).show();
    }
}
