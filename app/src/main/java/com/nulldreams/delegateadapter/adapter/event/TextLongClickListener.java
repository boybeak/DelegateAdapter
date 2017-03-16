package com.nulldreams.delegateadapter.adapter.event;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

import com.nulldreams.adapter.DelegateAdapter;
import com.nulldreams.adapter.widget.OnItemLongClickListener;
import com.nulldreams.delegateadapter.R;
import com.nulldreams.delegateadapter.adapter.TextDelegate;
import com.nulldreams.delegateadapter.adapter.TextHolder;

/**
 * Created by gaoyunfei on 2017/2/28.
 */

public class TextLongClickListener implements OnItemLongClickListener<TextDelegate, TextHolder> {
    @Override
    public boolean onLongClick(View view, Context context, TextDelegate textDelegate, TextHolder textHolder, int position, DelegateAdapter adapter) {
        if (view.getId() == R.id.text_tv) {
            Toast.makeText(context, TextHolder.class.getSimpleName() + " " + textDelegate.getSource(), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, TextHolder.class.getSimpleName(), Toast.LENGTH_SHORT).show();
        }
        return true;
    }
}
