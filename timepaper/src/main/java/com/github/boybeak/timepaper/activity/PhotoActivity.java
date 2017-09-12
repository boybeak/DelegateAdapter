package com.github.boybeak.timepaper.activity;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.Pair;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.OvershootInterpolator;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.github.boybeak.adapter.AbsDelegate;
import com.github.boybeak.adapter.DelegateAdapter;
import com.github.boybeak.timepaper.retrofit.Api;
import com.github.boybeak.timepaper.R;
import com.github.boybeak.timepaper.adapter.delegate.ExifDelegate;
import com.github.boybeak.timepaper.adapter.delegate.PhotoInfoDelegate;
import com.github.boybeak.timepaper.adapter.delegate.UserRowDelegate;
import com.github.boybeak.timepaper.adapter.holder.UserRowHolder;
import com.github.boybeak.timepaper.model.Photo;
import com.github.boybeak.timepaper.model.User;
import com.github.boybeak.timepaper.service.StreamService;
import com.github.boybeak.timepaper.utils.Intents;
import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.nulldreams.notify.toast.ToastCenter;

import java.io.File;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@SuppressWarnings("unchecked")
public class PhotoActivity extends BaseActivity {

    private static final String TAG = PhotoActivity.class.getSimpleName();

    private AppCompatImageView mThumbIv;
    private Toolbar mTb;
    private View mSetFabLayout;
    private FloatingActionButton mShareFab, mSetFab;
    private CircularProgressView mCpv;
    private RecyclerView mRv;

    private DelegateAdapter mAdapter;

    private Photo mPhoto;

    private StreamService mService;
    private org.xutils.common.Callback.Cancelable mCancelable;
    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            Log.v(TAG, "onServiceConnected ");
            mService = ((StreamService.StreamBinder)iBinder).getService();
            download();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            Log.v(TAG, "onServiceDisconnected ");
        }
    };

    private boolean isDownloading = false;
    private File mFile = null;
    private org.xutils.common.Callback.ProgressCallback<File> mFileCallback =
            new org.xutils.common.Callback.ProgressCallback<File>() {
        @Override
        public void onWaiting() {
            isDownloading = true;
            mSetFab.setImageResource(R.drawable.ic_close);
            showProgress();
        }

        @Override
        public void onStarted() {
            mCpv.startAnimation();
        }

        @Override
        public void onLoading(long total, long current, boolean isDownloading) {
            long progress = current * 100 / total;
            mCpv.setProgress(progress);
            Log.v(TAG, "onLoading " + progress);
        }

        @Override
        public void onSuccess(File result) {
            isDownloading = false;
            mSetFab.setImageResource(R.drawable.ic_format_paint);
            mCpv.stopAnimation();
            mFile = result;
        }

        @Override
        public void onError(Throwable ex, boolean isOnCallback) {
            isDownloading = false;
            mCpv.stopAnimation();
        }

        @Override
        public void onCancelled(CancelledException cex) {
            isDownloading = false;
            mCpv.stopAnimation();
        }

        @Override
        public void onFinished() {
            isDownloading = false;
            hideProgress();
        }
    };

    private View.OnClickListener mClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.photo_set_fab:
                    onSetFabClick(view);
                    break;
                case R.id.photo_share_fab:
                    Intents.shareText(PhotoActivity.this, "Share to:", mPhoto.links.html);
                    /*Intent share = new Intent(android.content.Intent.ACTION_SEND);
                    share.setType("text/plain");
                    User user = mPhoto.user;
                    share.putExtra(Intent.EXTRA_SUBJECT, user.first_name + " " + user.last_name);
                    share.putExtra(Intent.EXTRA_TEXT, mPhoto.links.html);

                    startActivity(Intent.createChooser(share, "Share link!"));*/
                    break;
            }
        }
    };

    private AbsDelegate.OnViewEventListener<User, UserRowHolder> userRowEventListener =
            new AbsDelegate.OnViewEventListener<User, UserRowHolder>() {
        @Override
        public void onViewEvent(int eventCode, final View view, final User t, final UserRowHolder viewHolder,
                                int position, DelegateAdapter adapter) {
            switch (eventCode) {
                case UserRowDelegate.PROFILE_CLICK:
                    if (isFabAnimating) {
                        return;
                    }
                    hideFabs(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animator) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animator) {
                            animator.removeAllListeners();
                            Intent it = new Intent(PhotoActivity.this, ProfileActivity.class);
                            Pair<View, String> p1 = new Pair<>((View) mThumbIv, getString(R.string.translation_name_photo));
                            Pair<View, String> p2 = new Pair<>(view, getString(R.string.translation_name_profile));
                            Pair<View, String> p3 = new Pair<>((View)viewHolder.nameTv, getString(R.string.translation_name_name));

                            ActivityOptionsCompat optionsCompat =
                                    ActivityOptionsCompat.makeSceneTransitionAnimation(PhotoActivity.this, p1, p2, p3);
                            it.putExtra("user", t);
                            it.putExtra("photo", mPhoto);
                            PhotoActivity.this.startActivity(it, optionsCompat.toBundle());
                        }

                        @Override
                        public void onAnimationCancel(Animator animator) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animator) {

                        }
                    });
                    break;
            }
        }
    };

    private boolean isFabsShowing = false;

    private float mShareTransX = 0, mSetTransX = 0;

    private boolean isFabAnimating = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        if (savedInstanceState == null) {
            mPhoto = getIntent().getParcelableExtra("photo");
        } else {
            mPhoto = savedInstanceState.getParcelable("photo");
        }

        Api.photoDetail(this, mPhoto).enqueue(new Callback<Photo>() {
            @Override
            public void onResponse(@NonNull Call<Photo> call, @NonNull Response<Photo> response) {
                String limit = response.headers().get("X-Ratelimit-Limit");
                String remaining = response.headers().get("X-Ratelimit-Remaining");
                Log.v(TAG, "limit=" + limit + " remaining=" + remaining);
                if (response.isSuccessful()) {
                    mPhoto = response.body();
                    refreshDetail();

                }
            }

            @Override
            public void onFailure(@NonNull Call<Photo> call, @NonNull Throwable t) {
                t.printStackTrace();
            }
        });

        mThumbIv = (AppCompatImageView)findViewById(R.id.photo_thumb);
        mThumbIv.setBackgroundColor(mPhoto.getBgColor());
        Glide.with(this).load(mPhoto.urls.regular)
                .transition(DrawableTransitionOptions.withCrossFade()).into(mThumbIv);

        mTb = (Toolbar)findViewById(R.id.photo_tb);
        setSupportActionBar(mTb);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);

            setTitle("");
        }

        mRv = (RecyclerView)findViewById(R.id.photo_info_rv);
        mAdapter = new DelegateAdapter(this);
        mRv.setAdapter(mAdapter);

        mSetFabLayout = findViewById(R.id.photo_set_fab_layout);
        mShareFab = (FloatingActionButton)findViewById(R.id.photo_share_fab);
        mSetFab = (FloatingActionButton)findViewById(R.id.photo_set_fab);
        mCpv = (CircularProgressView)findViewById(R.id.photo_progress);

        mSetFab.setOnClickListener(mClickListener);
        mShareFab.setOnClickListener(mClickListener);

        refreshDetail();

        getWindow().getDecorView().getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                getWindow().getDecorView().getViewTreeObserver().removeOnGlobalLayoutListener(this);
//                FrameLayout.LayoutParams params = (FrameLayout.LayoutParams)mSetFab.getLayoutParams();
                mSetTransX = mSetFabLayout.getWidth();
                mShareTransX = mSetTransX + mShareFab.getWidth();

                mSetFabLayout.setTranslationX(mSetTransX);
                mShareFab.setTranslationX(mShareTransX);

            }
        });

    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!isFabsShowing) {
            showFabs();
        }
    }

    @Override
    public void onBackPressed() {
        if (isFabAnimating) {
            return;
        }
        hideFabs(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                animator.removeAllListeners();
                PhotoActivity.super.onBackPressed();
            }

            @Override
            public void onAnimationCancel(Animator animator) {
                animator.removeAllListeners();
            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelable("photo", mPhoto);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onStop() {
        if (mCancelable != null && !mCancelable.isCancelled()) {
            mCancelable.cancel();
        }
        if (mService != null) {
            unbindService(mConnection);
            mService = null;
            Intent it = new Intent(this, StreamService.class);
            stopService(it);
        }
        super.onStop();
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
    }

    public void onSetFabClick(View view) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
            if (mService != null) {
                if (mFile != null && mFile.exists()) {
                    Intent intent = new Intent(Intent.ACTION_ATTACH_DATA);
                    intent.addCategory(Intent.CATEGORY_DEFAULT);
                    intent.setDataAndType(Uri.fromFile(mFile), "image/jpeg");
                    intent.putExtra("mimeType", "image/jpeg");
                    this.startActivity(Intent.createChooser(intent, "Set as:"));
                    return;
                }
                if (isDownloading) {
                    ToastCenter.with(PhotoActivity.this).text(R.string.toast_download_is_executing).showShort();
                    return;
                }
                download();
            } else {
                bindStreamService();
            }
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            bindStreamService();
        }
    }

    private void bindStreamService () {
        Intent it = new Intent(this, StreamService.class);
        startService(it);
        bindService(it, mConnection, Context.BIND_AUTO_CREATE);
    }

    private void download () {
        String url = mPhoto.urls.full;
        String name = mPhoto.id + ".jpg";

        File outputFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                "TimePaper" + File.separator + name);

        Log.v(TAG, "download url=" + url + " outputFile=" + outputFile.getAbsolutePath());
        mCancelable = mService.download(url, outputFile.getAbsolutePath(), mFileCallback);
    }

    private void refreshDetail () {
        mAdapter.clear();
        mAdapter.notifyDataSetChanged();
        mAdapter.add(new UserRowDelegate(mPhoto.user, userRowEventListener)).autoNotify();
        if (!TextUtils.isEmpty(mPhoto.description) || mPhoto.location != null) {
            mAdapter.add(new PhotoInfoDelegate(mPhoto)).autoNotify();
        }
        if (mPhoto.exif != null && mPhoto.exif.isValid()) {
            mAdapter.add(new ExifDelegate(mPhoto.exif)).autoNotify();
        }

    }

    private void showFabs () {
        isFabAnimating = true;
        isFabsShowing = true;
        mSetFabLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                final AnimatorSet animatorSet = new AnimatorSet();

                ObjectAnimator shareAnim = ObjectAnimator.ofFloat(mShareFab, "translationX", mShareFab.getTranslationX(), 0);
                ObjectAnimator setAnim = ObjectAnimator.ofFloat(mSetFabLayout, "translationX", mSetFabLayout.getTranslationX(), 0);

                animatorSet.play(shareAnim).with(setAnim);
                animatorSet.setInterpolator(new OvershootInterpolator());
                animatorSet.setDuration(getResources().getInteger(android.R.integer.config_mediumAnimTime));
                animatorSet.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animator) {
                        isFabAnimating = false;
                    }

                    @Override
                    public void onAnimationCancel(Animator animator) {
                        isFabAnimating = false;
                    }

                    @Override
                    public void onAnimationRepeat(Animator animator) {

                    }
                });
                animatorSet.start();
            }
        }, getResources().getInteger(android.R.integer.config_mediumAnimTime));
    }

    private void hideFabs (Animator.AnimatorListener listener) {
        final AnimatorSet animatorSet = new AnimatorSet();

        ObjectAnimator shareAnim = ObjectAnimator.ofFloat(mShareFab, "translationX", mShareFab.getTranslationX(), mShareTransX);
        ObjectAnimator setAnim = ObjectAnimator.ofFloat(mSetFabLayout, "translationX", mSetFabLayout.getTranslationX(), mSetTransX);

        animatorSet.play(shareAnim).with(setAnim);
        animatorSet.setInterpolator(new AccelerateInterpolator());
        animatorSet.setDuration(getResources().getInteger(android.R.integer.config_shortAnimTime));

        animatorSet.addListener(listener);
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                isFabAnimating = false;
                animatorSet.removeAllListeners();
            }

            @Override
            public void onAnimationCancel(Animator animator) {
                isFabAnimating = false;
            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        animatorSet.start();
        isFabAnimating = true;
        isFabsShowing = false;
    }

    private ObjectAnimator mRotate = null;
    private void showProgress () {
        ObjectAnimator alpha = ObjectAnimator.ofFloat(mCpv, "alpha", 0f, 1f);

        alpha.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                mCpv.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                animator.removeAllListeners();
            }

            @Override
            public void onAnimationCancel(Animator animator) {
                animator.removeAllListeners();
            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        alpha.start();

        mSetFab.setPivotX(mSetFab.getWidth() * 1f / 2);
        mSetFab.setPivotY(mSetFab.getHeight() * 1f / 2);
        mRotate = ObjectAnimator.ofFloat(mSetFab, "rotation", 0, 360);
        mRotate.setDuration(2000);
        mRotate.setRepeatMode(ValueAnimator.RESTART);
        mRotate.setRepeatCount(1000);
        mRotate.start();
    }

    private void hideProgress () {

        if (mRotate != null && mRotate.isRunning()) {
            mRotate.cancel();
            mSetFab.setPivotX(mSetFab.getWidth() * 1f / 2);
            mSetFab.setPivotY(mSetFab.getHeight() * 1f / 2);
            mRotate = ObjectAnimator.ofFloat(mSetFab, "rotation", mSetFab.getRotation(), 0);
            mRotate.setDuration(getResources().getInteger(android.R.integer.config_shortAnimTime));
            mRotate.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {

                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    mSetFab.setRotation(0);
                    mRotate = null;
                }

                @Override
                public void onAnimationCancel(Animator animator) {

                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            });
            mRotate.start();
        }

        ObjectAnimator alpha = ObjectAnimator.ofFloat(mCpv, "alpha", 1f, 0f);

        alpha.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                mCpv.setVisibility(View.GONE);
                animator.removeAllListeners();
            }

            @Override
            public void onAnimationCancel(Animator animator) {
                animator.removeAllListeners();
            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        alpha.start();
    }

}