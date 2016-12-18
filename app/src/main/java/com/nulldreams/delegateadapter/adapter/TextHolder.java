package com.nulldreams.delegateadapter.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.nulldreams.adapter.adapter.AbsViewHolder;
import com.nulldreams.adapter.adapter.DelegateAdapter;
import com.nulldreams.delegateadapter.R;

/**
 * Created by gaoyunfei on 2016/12/18.
 */

public class TextHolder extends AbsViewHolder<TextDelegate> {

    private TextView tv;

    public TextHolder(View itemView) {
        super(itemView);
        tv = (TextView)itemView.findViewById(R.id.text_tv);
    }

    @Override
    public void onBindView(Context context, TextDelegate textDelegate, int position, DelegateAdapter adapter) {
        tv.setText(textDelegate.getSource() + " " + getClass().getSimpleName());
    }
}
