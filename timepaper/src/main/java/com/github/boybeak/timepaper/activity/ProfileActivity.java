package com.github.boybeak.timepaper.activity;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.view.MenuItem;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.github.boybeak.adapter.AbsDelegate;
import com.github.boybeak.adapter.DelegateAdapter;
import com.github.boybeak.adapter.DelegateParser;
import com.github.boybeak.adapter.extention.SuperAdapter;
import com.github.boybeak.adapter.extention.callback.OnScrollBottomListener;
import com.github.boybeak.adapter.impl.LayoutImpl;
import com.github.boybeak.timepaper.R;
import com.github.boybeak.timepaper.adapter.StateSpanLookup;
import com.github.boybeak.timepaper.adapter.delegate.EmptyDelegate;
import com.github.boybeak.timepaper.adapter.delegate.FooterDelegate;
import com.github.boybeak.timepaper.adapter.delegate.PhotoMiniDelegate;
import com.github.boybeak.timepaper.adapter.holder.EmptyHolder;
import com.github.boybeak.timepaper.model.Photo;
import com.github.boybeak.timepaper.model.User;
import com.github.boybeak.timepaper.retrofit.Api;
import com.github.boybeak.timepaper.utils.Intents;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends BaseActivity {

    private User mUser;
    private Photo mPhoto;

    private View mProfileView, mMaskView, mCountView, mPhotoCountView,
            mFollowerCountView, mFollowingCountView, mSocialLayout;
    private AppCompatImageView mProfileImageIv, mCoverIv, mMailIv, mTwitterIv, mInsIv;
    private Toolbar mTb;
    private AppCompatTextView mNameTv, mBioTv;
    private RecyclerView mRv;

    private SuperAdapter<EmptyDelegate, FooterDelegate> mAdapter;

    private AbsDelegate.OnViewEventListener<String, EmptyHolder> mEmptyListener =
            new AbsDelegate.OnViewEventListener<String, EmptyHolder>() {
                @Override
                public void onViewEvent(int eventCode, View view, String t, EmptyHolder viewHolder, int position, DelegateAdapter adapter) {

                }
            };

    private OnScrollBottomListener mBottomListener = new OnScrollBottomListener() {
        @Override
        public void onScrollBottom(RecyclerView recyclerView, int newState) {
            if (!isLoading && !isAllLoaded) {
                showUserPhotos(mUser);
            }
        }
    };

    private View.OnClickListener mClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.profile_email:
                    Intents.openUrl(ProfileActivity.this, mUser.email);
                    break;
                case R.id.profile_twitter:
                    Intents.openUrl(ProfileActivity.this, mUser.getTwitterUrl());
                    break;
                case R.id.profile_instagram:
                    Intents.openUrl(ProfileActivity.this, mUser.getInstagramUrl());
                    break;
            }
        }
    };

    private int mPage = 1;
    private boolean isLoading = false;

    private boolean isAllLoaded = false;

    private Call<User> mUserCall;
    private Call<List<Photo>> mPhotoCall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        if (savedInstanceState == null) {
            mUser = getIntent().getParcelableExtra("user");
            mPhoto = getIntent().getParcelableExtra("photo");
        } else {
            mUser = savedInstanceState.getParcelable("user");
            mPhoto = savedInstanceState.getParcelable("photo");
        }

        mProfileView = findViewById(R.id.profile_layout);
        mMaskView = findViewById(R.id.profile_mask);

        mSocialLayout = findViewById(R.id.profile_social);
        mMailIv = mSocialLayout.findViewById(R.id.profile_email);
        mTwitterIv = mSocialLayout.findViewById(R.id.profile_twitter);
        mInsIv = mSocialLayout.findViewById(R.id.profile_instagram);

        mCountView = findViewById(R.id.profile_count_layout);
        mPhotoCountView = findViewById(R.id.profile_photo_count);
        mFollowerCountView = findViewById(R.id.profile_follower_count);
        mFollowingCountView = findViewById(R.id.profile_following_count);

        mProfileImageIv = (AppCompatImageView)findViewById(R.id.profile_image);
        mCoverIv = (AppCompatImageView)findViewById(R.id.profile_cover);
        mNameTv = (AppCompatTextView)findViewById(R.id.profile_name);
        mBioTv = (AppCompatTextView)findViewById(R.id.profile_bio);

        mTb = (Toolbar)findViewById(R.id.profile_tb);
        setSupportActionBar(mTb);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            setTitle("");
        }

        mRv = (RecyclerView)findViewById(R.id.profile_photo_rv);
        mRv.addOnScrollListener(mBottomListener);

        mAdapter = new SuperAdapter<EmptyDelegate, FooterDelegate>(this);
        GridLayoutManager gridLayoutManager = (GridLayoutManager)mRv.getLayoutManager();
        gridLayoutManager.setSpanSizeLookup(new StateSpanLookup(mAdapter, gridLayoutManager.getSpanCount()));
        mAdapter.setEmptyItem(new EmptyDelegate("", mEmptyListener));
        mAdapter.setTailItem(new FooterDelegate(""));
        mRv.setAdapter(mAdapter);

        mUserCall = Api.userProfile(this, mUser);
        mUserCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                if (response.isSuccessful()) {
                    User user = response.body();
                    if (user != null) {
                        mUser = user;
                        showUser(mUser);
                    }
                } else {

                }
            }

            @Override
            public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {

            }
        });
        showUser(mUser);
        showUserPhotos(mUser);

        if (mPhoto != null) {
            mCoverIv.setBackgroundColor(mPhoto.getBgColor());
            Glide.with(this).load(mPhoto.urls.regular)
                    .transition(DrawableTransitionOptions.withCrossFade()).into(mCoverIv);
        }

    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        animCoverBg();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelable("user", mUser);
        if (mPhoto != null) {
            outState.putParcelable("photo", mPhoto);
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mRv.removeOnScrollListener(mBottomListener);

        if (mUserCall != null && !mUserCall.isCanceled()) {
            mUserCall.cancel();
        }
        if (mPhotoCall != null && !mPhotoCall.isCanceled()) {
            mPhotoCall.cancel();
        }
    }

    private void showUser (User user) {
        Glide.with(this).load(user.profile_image.large)
                .apply(RequestOptions.placeholderOf(R.drawable.ic_account_circle))
                .apply(RequestOptions.circleCropTransform())
                .into(mProfileImageIv);
        mNameTv.setText(user.getFullName());
        mBioTv.setText(user.bio);
        showCount(mPhotoCountView, user.total_photos, R.string.text_count_photos);
        showCount(mFollowerCountView, user.followers_count, R.string.text_count_followers);
        showCount(mFollowingCountView, user.following_count, R.string.text_count_following);

        if (user.hasSocialMedia()) {
            mSocialLayout.setVisibility(View.VISIBLE);
            mMailIv.setVisibility(user.hasEmail() ? View.VISIBLE : View.GONE);
            mTwitterIv.setVisibility(user.hasTwitter() ? View.VISIBLE : View.GONE);
            mInsIv.setVisibility(user.hasInstagram() ? View.VISIBLE : View.GONE);

            mMailIv.setOnClickListener(mClickListener);
            mTwitterIv.setOnClickListener(mClickListener);
            mInsIv.setOnClickListener(mClickListener);
        } else {
            mSocialLayout.setVisibility(View.GONE);
        }
    }

    private void showUserPhotos (User user) {
        isLoading = true;
        mAdapter.getTailItem().notifyLoadingState();
        mAdapter.notifyTail();
        mPhotoCall = Api.userPhotos(this, user, mPage);
        mPhotoCall.enqueue(new Callback<List<Photo>>() {
            @Override
            public void onResponse(@NonNull Call<List<Photo>> call, @NonNull Response<List<Photo>> response) {
                isLoading = false;
                if (response.isSuccessful()) {
                    int total = Integer.parseInt(response.headers().get("x-total"));
                    isAllLoaded = mAdapter.getItemCountExceptEmptyAndTail() >= total;
                    List<Photo> photos = response.body();
                    if (photos != null && !photos.isEmpty()) {
                        if (mPage == 1) {
                            mAdapter.clear();
                            mAdapter.notifyDataSetChangedSafety();
                        }
                        mAdapter.addAll(photos, new DelegateParser<Photo>() {
                            @Override
                            public LayoutImpl parse(DelegateAdapter adapter, Photo data) {
                                return new PhotoMiniDelegate(data);
                            }
                        }).autoNotify();
                        mAdapter.getTailItem().notifySuccessState();
                        mAdapter.notifyTail();
                        mPage++;
                    } else {
                        if (mAdapter.containsTailItem()) {
                            mAdapter.getTailItem().notifyFailedState();
                            mAdapter.notifyTail();
                        } else if (mAdapter.onlyContainsEmptyItem()) {
                            mAdapter.getEmptyItem().notifyEmptyState();
                            mAdapter.notifyEmpty();
                        }
                    }
                } else {
                    if (mAdapter.containsTailItem()) {
                        mAdapter.getTailItem().notifyFailedState();
                        mAdapter.notifyTail();
                    } else if (mAdapter.onlyContainsEmptyItem()) {
                        mAdapter.getEmptyItem().notifyEmptyState();
                        mAdapter.notifyEmpty();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Photo>> call, @NonNull Throwable t) {
                isLoading = false;
                if (mAdapter.containsTailItem()) {
                    mAdapter.getTailItem().notifyFailedState();
                    mAdapter.notifyTail();
                } else if (mAdapter.onlyContainsEmptyItem()) {
                    mAdapter.getEmptyItem().notifyEmptyState();
                    mAdapter.notifyEmpty();
                }

            }
        });
    }

    private void animCoverBg () {
        ObjectAnimator animator = ObjectAnimator.ofFloat(mMaskView, "alpha", 0, 1);
        animator.setDuration(getResources().getInteger(android.R.integer.config_longAnimTime));
        animator.setStartDelay(1000);
        animator.start();
    }

    private void showCount (View countView, int count, int title) {
        AppCompatTextView top = countView.findViewById(R.id.vertical_text_top);
        AppCompatTextView bottom = countView.findViewById(R.id.vertical_text_bottom);

        top.setText(count + "");
        bottom.setText(title);
    }
}
