package com.github.boybeak.timepaper.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.github.boybeak.timepaper.R;
import com.github.boybeak.timepaper.fragment.GalleryPagerFragment;
import com.github.boybeak.timepaper.model.Photo;

import java.util.List;

public class GalleryActivity extends BaseActivity {

    private ViewPager mVp;

    private List<Photo> mPhotos;

    private GalleryAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        mPhotos = getIntent().getParcelableArrayListExtra("photos");
        int index = getIntent().getIntExtra("index", 0);

        mVp = (ViewPager)findViewById(R.id.gallery_vp);
        mAdapter = new GalleryAdapter(getSupportFragmentManager());
        mVp.setAdapter(mAdapter);

        mVp.setCurrentItem(index);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    private class GalleryAdapter extends FragmentStatePagerAdapter {

        public GalleryAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            GalleryPagerFragment fragment = new GalleryPagerFragment();
            Bundle bundle = new Bundle();
            bundle.putParcelable("photo", mPhotos.get(position));
            bundle.putInt("index", position);
            fragment.setArguments(bundle);
            return fragment;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Object object = super.instantiateItem(container, position);
            if (object instanceof GalleryPagerFragment) {
                GalleryPagerFragment fragment = (GalleryPagerFragment)object;
                Bundle bundle = new Bundle();
                bundle.putParcelable("photo", mPhotos.get(position));
                bundle.putInt("index", position);
                fragment.setArguments(bundle);
            }
            return object;
        }

        @Override
        public int getCount() {
            return mPhotos.size();
        }
    }
}