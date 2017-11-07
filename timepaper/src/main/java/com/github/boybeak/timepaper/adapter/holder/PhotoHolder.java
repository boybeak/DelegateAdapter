package com.github.boybeak.timepaper.adapter.holder;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions;
import com.github.boybeak.adapter.AbsViewHolder;
import com.github.boybeak.adapter.DelegateAdapter;
import com.github.boybeak.timepaper.R;
import com.github.boybeak.timepaper.activity.ProfileActivity;
import com.github.boybeak.timepaper.adapter.delegate.PhotoDelegate;
import com.github.boybeak.timepaper.model.Photo;

/**
 * Created by gaoyunfei on 2017/9/5.
 */

public class PhotoHolder extends AbsViewHolder<PhotoDelegate> {

    private AppCompatImageView thumbIv;
    private AppCompatTextView authorTv;

    private BitmapTransitionOptions options;

    private View.OnClickListener clickListener = null;

    public PhotoHolder(View itemView) {
        super(itemView);

        thumbIv = findViewById(R.id.photo_thumb);
        authorTv = findViewById(R.id.photo_author);
        options = BitmapTransitionOptions.withCrossFade();
    }

    @Override
    public void onBindView(final Context context, final PhotoDelegate t, final int position, final DelegateAdapter adapter) {
        final Photo photo = t.getSource();
        itemView.setBackgroundColor(photo.getBgColor());
        authorTv.setText(context.getString(R.string.text_unsplash, photo.user.getFullName()));
        Glide.with(context).asBitmap().load(photo.urls.regular)
                .transition(options).into(thumbIv);

        clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final int id = view.getId();
                if (id == itemView.getId()) {
                    t.actionViewEvent(
                            PhotoDelegate.EVENT_CLICK,
                            view, PhotoHolder.this, position, adapter);
                } else if (id == authorTv.getId()) {
                    Intent it = new Intent(context, ProfileActivity.class);
                    it.putExtra("photo", photo);
                    it.putExtra("user", photo.user);
                    context.startActivity(it);
                }
            }
        };
        authorTv.setOnClickListener(clickListener);
        itemView.setOnClickListener(clickListener);
    }
}
