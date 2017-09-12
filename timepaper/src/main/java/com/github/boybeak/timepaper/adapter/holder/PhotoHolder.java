package com.github.boybeak.timepaper.adapter.holder;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.github.boybeak.adapter.AbsViewHolder;
import com.github.boybeak.adapter.DelegateAdapter;
import com.github.boybeak.timepaper.R;
import com.github.boybeak.timepaper.adapter.delegate.PhotoDelegate;
import com.github.boybeak.timepaper.model.Photo;

/**
 * Created by gaoyunfei on 2017/9/5.
 */

public class PhotoHolder extends AbsViewHolder<PhotoDelegate> {

    private AppCompatImageView thumbIv;

    private DrawableTransitionOptions options;

    public PhotoHolder(View itemView) {
        super(itemView);

        thumbIv = findViewById(R.id.photo_thumb);
        options = DrawableTransitionOptions.withCrossFade();
    }

    @Override
    public void onBindView(Context context, final PhotoDelegate t, final int position, final DelegateAdapter adapter) {
        final Photo photo = t.getSource();
        itemView.setBackgroundColor(photo.getBgColor());
        Glide.with(context).load(photo.urls.regular).transition(options).into(thumbIv);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                t.actionViewEvent(
                        PhotoDelegate.EVENT_CLICK,
                        view, PhotoHolder.this, photo, position, adapter);
            }
        });
    }
}
