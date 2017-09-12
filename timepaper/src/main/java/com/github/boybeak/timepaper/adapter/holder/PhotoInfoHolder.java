package com.github.boybeak.timepaper.adapter.holder;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.view.View;

import com.github.boybeak.adapter.AbsViewHolder;
import com.github.boybeak.adapter.DelegateAdapter;
import com.github.boybeak.timepaper.R;
import com.github.boybeak.timepaper.adapter.delegate.PhotoInfoDelegate;
import com.github.boybeak.timepaper.model.Location;
import com.github.boybeak.timepaper.model.Photo;

/**
 * Created by gaoyunfei on 2017/9/6.
 */

public class PhotoInfoHolder extends AbsViewHolder<PhotoInfoDelegate> {

    private AppCompatTextView photoInfoDescTv, photoInfoLocationTv;

    public PhotoInfoHolder(View itemView) {
        super(itemView);

        photoInfoDescTv = findViewById(R.id.photo_info_description);
        photoInfoLocationTv = findViewById(R.id.photo_info_location);
    }

    @Override
    public void onBindView(Context context, PhotoInfoDelegate t, int position, DelegateAdapter adapter) {

        Photo photo = t.getSource();

        photoInfoDescTv.setVisibility(TextUtils.isEmpty(photo.description) ? View.GONE : View.VISIBLE);
        photoInfoDescTv.setText(photo.description);

        Location location = photo.location;
        if (location != null) {
            photoInfoLocationTv.setVisibility(View.VISIBLE);
            photoInfoLocationTv.setText(location.title);
        } else {
            photoInfoLocationTv.setVisibility(View.GONE);
        }

    }
}
