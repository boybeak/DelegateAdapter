package com.github.boybeak.timepaper.activity;

import android.app.WallpaperManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.github.boybeak.timepaper.Finals;
import com.github.boybeak.timepaper.R;
import com.github.boybeak.timepaper.fragment.LatestFragment;
import com.github.boybeak.timepaper.fragment.PaperFragment;
import com.github.boybeak.timepaper.fragment.PopularFragment;
import com.github.boybeak.timepaper.fragment.RandomFragment;
import com.github.boybeak.timepaper.model.User;
import com.github.boybeak.timepaper.retrofit.Api;
import com.github.boybeak.timepaper.retrofit.Auth;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends BaseActivity {

    private CoordinatorLayout mCl;
    private AppCompatImageView mCoverIv;
    private Toolbar mTb;
    private TabLayout mTl;
    private ViewPager mVp;

    private PaperAdapter mAdapter;

    private PaperFragment[] mPapers = new PaperFragment[3];

    private TabLayout.OnTabSelectedListener mTabListener = new TabLayout.OnTabSelectedListener() {
        @Override
        public void onTabSelected(TabLayout.Tab tab) {

        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {

        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {
            mPapers[tab.getPosition()].scrollToTop();
        }
    };

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case Intent.ACTION_WALLPAPER_CHANGED:
                    refreshWallpaper();
                    break;
                case Finals.ACTION_LOGOUT:

                    break;
            }

        }
    };

    private BroadcastReceiver mLogoutReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (Finals.ACTION_LOGOUT.equals(intent.getAction())) {
                finish();
                startActivity(new Intent(MainActivity.this, HelloActivity.class));
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mCl = (CoordinatorLayout)findViewById(R.id.main_cl);
        mCoverIv = (AppCompatImageView)findViewById(R.id.main_tb_cover);
        mTb = (Toolbar)findViewById(R.id.main_tb);
        mTl = (TabLayout)findViewById(R.id.main_tab_layout);
        mVp = (ViewPager)findViewById(R.id.main_view_pager);

        mTb.setNavigationIcon(R.mipmap.ic_launcher_round);
        setSupportActionBar(mTb);

        mPapers[0] = new RandomFragment();
        mPapers[1] = new LatestFragment();
        mPapers[2] = new PopularFragment();

        mAdapter = new PaperAdapter(getSupportFragmentManager());
        mVp.setAdapter(mAdapter);
        mVp.setOffscreenPageLimit(mPapers.length);
        mVp.setCurrentItem(1);
        mTl.setupWithViewPager(mVp);

        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE |
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        );

        mTl.addOnTabSelectedListener(mTabListener);

        if (Auth.hasMe(this)) {
            showMe(Auth.getMe(this));
        }
        Auth.syncMe(this, new Auth.UserListener() {
            @Override
            public void onUserInfo(User user) {
                showMe(user);
            }

            @Override
            public void onUserNotExist() {

            }
        });

        refreshWallpaper();

        registerReceiver(mReceiver, new IntentFilter(Intent.ACTION_WALLPAPER_CHANGED));
        LocalBroadcastManager.getInstance(this).registerReceiver(mLogoutReceiver,
                new IntentFilter(Finals.ACTION_LOGOUT));
    }

    private void refreshWallpaper () {
        final WallpaperManager wallpaperManager = WallpaperManager.getInstance(this);
        final Drawable wallpaperDrawable = wallpaperManager.getDrawable();
        mCoverIv.setImageDrawable(wallpaperDrawable);
    }

    private void showMe (final User me) {
        int iconSize = getResources().getDimensionPixelSize(R.dimen.navigation_icon_size);
        Glide.with(MainActivity.this).load(me.profile_image.large)
                .transition(DrawableTransitionOptions.withCrossFade())
                .apply(RequestOptions.circleCropTransform())
                .apply(RequestOptions.overrideOf(iconSize, iconSize))
                .apply(RequestOptions.errorOf(R.mipmap.ic_launcher_round))
                .into(new SimpleTarget<Drawable>() {
                    @Override
                    public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
                        mTb.setNavigationIcon(resource);
                    }
                });
        mTb.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(MainActivity.this, ProfileActivity.class);
                it.putExtra("user", me);
                startActivity(it);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.main_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mTl.removeOnTabSelectedListener(mTabListener);
        unregisterReceiver(mReceiver);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mLogoutReceiver);
    }

    private class PaperAdapter extends FragmentStatePagerAdapter {

        public PaperAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    if (mPapers[0] == null) {
                        mPapers[0] = new RandomFragment();
                    }
                    break;
                case 1:
                    if (mPapers[1] == null) {
                        mPapers[1] = new LatestFragment();
                    }
                    break;
                case 2:
                    if (mPapers[2] == null) {
                        mPapers[2] = new PopularFragment();
                    }
                    break;
            }
            return mPapers[position];
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Object object = super.instantiateItem(container, position);
            if (object instanceof PaperFragment) {
                PaperFragment fragment = (PaperFragment)object;
                mPapers[position] = fragment;
            }
            return object;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mPapers[position].getTitle(MainActivity.this);
        }

        @Override
        public int getCount() {
            return mPapers.length;
        }
    }
}
