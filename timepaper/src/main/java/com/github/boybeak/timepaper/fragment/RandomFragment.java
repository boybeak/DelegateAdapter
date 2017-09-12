package com.github.boybeak.timepaper.fragment;

import android.content.Context;
import android.support.annotation.NonNull;

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

public class RandomFragment extends PaperFragment {

    protected boolean shouldClear = false;

    @Override
    public CharSequence getTitle(Context context) {
        return context.getString(R.string.title_pager_random);
    }

    @Override
    public void onResponse(List<Photo> photos, SuperAdapter<EmptyDelegate, FooterDelegate> adapter) {
        if (shouldClear) {
            adapter.clear();
            adapter.notifyDataSetChangedSafety();
            shouldClear = false;
        }
        super.onResponse(photos, adapter);
    }

    @Override
    public void onFailure(@NonNull Call<List<Photo>> call, @NonNull Throwable t, SuperAdapter<EmptyDelegate, FooterDelegate> adapter) {

    }

    @Override
    public void onLoadData() {
        super.onLoadData();
        Api.randomPhotos(getContext(), Api.PORTRAIT).enqueue(this);
    }

    @Override
    public void onRefresh() {
        super.onRefresh();
        shouldClear = true;
    }
}
