package com.nulldreams.delegateadapter.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nulldreams.adapter.adapter.AbsViewHolder;
import com.nulldreams.adapter.adapter.DelegateAdapter;
import com.nulldreams.delegateadapter.R;

/**
 * Created by gaoyunfei on 2016/12/18.
 */

public class ColorHolder extends AbsViewHolder<ColorDelegate> {
    private ImageView iv;
    private TextView classNameTv;
    public ColorHolder(View itemView) {
        super(itemView);
        iv = (ImageView)itemView.findViewById(R.id.iv);
        classNameTv = (TextView)itemView.findViewById(R.id.class_name_tv);
    }

    @Override
    public void onBindView(Context context, ColorDelegate colorDelegate, int position, DelegateAdapter adapter) {
        iv.setBackgroundColor(colorDelegate.getSource());
        classNameTv.setText(this.getClass().getSimpleName());
    }
}
