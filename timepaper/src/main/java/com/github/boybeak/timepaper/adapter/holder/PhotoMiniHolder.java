package com.github.boybeak.timepaper.adapter.holder;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.github.boybeak.adapter.AbsViewHolder;
import com.github.boybeak.adapter.DelegateAdapter;
import com.github.boybeak.timepaper.R;
import com.github.boybeak.timepaper.adapter.delegate.PhotoMiniDelegate;
import com.github.boybeak.timepaper.model.Photo;

/**
 * Created by gaoyunfei on 2017/9/9.
 */

public class PhotoMiniHolder extends AbsViewHolder<PhotoMiniDelegate> {

    private AppCompatImageView thumbIv;

    private DrawableTransitionOptions mOptions;

    public PhotoMiniHolder(View itemView) {
        super(itemView);

        thumbIv = findViewById(R.id.photo_mini_thumb);
        mOptions = DrawableTransitionOptions.withCrossFade();
    }

    @Override
    public void onBindView(Context context, PhotoMiniDelegate t, int position, DelegateAdapter adapter) {
        Photo photo = t.getSource();
        thumbIv.setBackgroundColor(photo.getBgColor());
        Glide.with(context).load(photo.urls.small).transition(mOptions).into(thumbIv);
    }
}
