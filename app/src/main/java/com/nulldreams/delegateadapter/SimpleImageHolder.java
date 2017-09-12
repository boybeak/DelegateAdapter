package com.nulldreams.delegateadapter;

import android.content.Context;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatImageView;
import android.view.View;
import android.widget.CompoundButton;

import com.bumptech.glide.Glide;
import com.github.boybeak.adapter.AbsViewHolder;
import com.github.boybeak.adapter.DelegateAdapter;
import com.github.boybeak.adapter.extention.SuperAdapter;

/**
 * Created by gaoyunfei on 2017/8/30.
 */

public class SimpleImageHolder extends AbsViewHolder<SimpleImageDelegate> {

    private AppCompatImageView thumbIv;
    private AppCompatCheckBox checkBox;

    public SimpleImageHolder(View itemView) {
        super(itemView);

        thumbIv = findViewById(R.id.thumb_iv);
        checkBox = findViewById(R.id.checkbox);
    }

    @Override
    public void onBindView(Context context, final SimpleImageDelegate simpleImageDelegate,
                           final int position, final DelegateAdapter adapter) {
        final SimpleImage image = simpleImageDelegate.getSource();
        Glide.with(context).asBitmap().load(image.getData()).into(thumbIv);
        final SuperAdapter exAdapter = (SuperAdapter)adapter;

        checkBox.setVisibility(exAdapter.multipleControl().isStarted() ? View.VISIBLE : View.GONE);
        checkBox.setOnCheckedChangeListener(null);
        checkBox.setChecked(simpleImageDelegate.isChecked());
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                exAdapter.multipleControl().check(simpleImageDelegate);
            }
        });
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (exAdapter.multipleControl().isStarted()) {
                    exAdapter.multipleControl().check(simpleImageDelegate);
                } else {
                    simpleImageDelegate.actionViewEvent(0, view, SimpleImageHolder.this, image, position, adapter);
                }

            }
        });
        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                simpleImageDelegate.actionViewEvent(1, view, SimpleImageHolder.this, image, position, adapter);
                exAdapter.multipleControl().check(simpleImageDelegate);
                return true;
            }
        });
    }

}
