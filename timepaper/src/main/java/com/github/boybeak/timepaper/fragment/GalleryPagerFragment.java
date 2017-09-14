package com.github.boybeak.timepaper.fragment;

import android.graphics.PointF;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.github.boybeak.timepaper.R;
import com.github.boybeak.timepaper.model.Photo;

import java.io.File;

/**
 * Created by gaoyunfei on 2017/9/13.
 */

public class GalleryPagerFragment extends Fragment {

    private static final String TAG = GalleryPagerFragment.class.getSimpleName();

    private SubsamplingScaleImageView mScaleImageView;
    private AppCompatTextView mIndexTv;
    private ProgressBar mPb;

    private SubsamplingScaleImageView.OnImageEventListener mEventListener =
            new SubsamplingScaleImageView.OnImageEventListener() {
        @Override
        public void onReady() {

        }

        @Override
        public void onImageLoaded() {
            mPb.setVisibility(View.GONE);
        }

        @Override
        public void onPreviewLoadError(Exception e) {

        }

        @Override
        public void onImageLoadError(Exception e) {

        }

        @Override
        public void onTileLoadError(Exception e) {

        }

        @Override
        public void onPreviewReleased() {

        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_gallery_pager, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mPb = view.findViewById(R.id.gallery_pager_pb);
        mScaleImageView = view.findViewById(R.id.gallery_pager_iv);
        mIndexTv = view.findViewById(R.id.gallery_pager_index);

        Photo photo = getArguments().getParcelable("photo");
        int index = getArguments().getInt("index");
        mIndexTv.setText(String.valueOf(index + 1));

        loadPhoto(photo);

        mScaleImageView.setOnImageEventListener(mEventListener);

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        Photo photo = getArguments().getParcelable("photo");
        loadPhoto(photo);
    }

    private void loadPhoto(Photo photo) {
        if (photo != null && getUserVisibleHint() && isAdded() && !mScaleImageView.isImageLoaded()) {
            mPb.setVisibility(View.VISIBLE);
            Glide.with(this).asFile().load(photo.urls.full).into(new SimpleTarget<File>() {
                @Override
                public void onResourceReady(File resource, Transition<? super File> transition) {
                    ImageSource source = ImageSource.uri(Uri.fromFile(resource));
                    mScaleImageView.setImage(source);
                }
            });
        }
    }
}
