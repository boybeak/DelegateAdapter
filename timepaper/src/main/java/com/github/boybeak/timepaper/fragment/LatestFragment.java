package com.github.boybeak.timepaper.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.github.boybeak.adapter.extention.SuperAdapter;
import com.github.boybeak.timepaper.retrofit.Api;
import com.github.boybeak.timepaper.R;
import com.github.boybeak.timepaper.adapter.delegate.EmptyDelegate;
import com.github.boybeak.timepaper.adapter.delegate.FooterDelegate;
import com.github.boybeak.timepaper.model.Photo;

import java.util.List;

import retrofit2.Call;

/**
 * Created by gaoyunfei on 2017/9/5.
 */

public class LatestFragment extends PaperFragment {

    private int mPage = 1;

    @Override
    public CharSequence getTitle(Context context) {
        return context.getString(R.string.title_pager_latest);
    }

    @Override
    public void onResponse(List<Photo> photos, SuperAdapter<EmptyDelegate, FooterDelegate> adapter) {
        if (mPage == 1) {
            adapter.clear();
            adapter.notifyDataSetChangedSafety();
        }
        super.onResponse(photos, adapter);
        mPage++;
    }

    @Override
    public void onFailure(@NonNull Call<List<Photo>> call, @NonNull Throwable t, SuperAdapter<EmptyDelegate, FooterDelegate> adapter) {

    }

    @Override
    public void onLoadData() {
        super.onLoadData();
        Api.photos(getContext(), mPage, 10, Api.LATEST).enqueue(this);
    }

    @Override
    public void onRefresh() {
        mPage = 1;
        super.onRefresh();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt("page", mPage);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            mPage = savedInstanceState.getInt("page");
        }
        super.onViewStateRestored(savedInstanceState);
    }
}
