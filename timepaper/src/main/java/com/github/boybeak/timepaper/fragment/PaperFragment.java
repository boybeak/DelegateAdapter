package com.github.boybeak.timepaper.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.boybeak.adapter.AbsDelegate;
import com.github.boybeak.adapter.DelegateAdapter;
import com.github.boybeak.adapter.DelegateParser;
import com.github.boybeak.adapter.OnViewEventListener;
import com.github.boybeak.adapter.extension.SuperAdapter;
import com.github.boybeak.adapter.extension.callback.OnScrollBottomListener;
import com.github.boybeak.adapter.impl.LayoutImpl;
import com.github.boybeak.selector.Path;
import com.github.boybeak.timepaper.R;
import com.github.boybeak.timepaper.activity.PhotoActivity;
import com.github.boybeak.timepaper.adapter.delegate.EmptyDelegate;
import com.github.boybeak.timepaper.adapter.delegate.FooterDelegate;
import com.github.boybeak.timepaper.adapter.delegate.PhotoDelegate;
import com.github.boybeak.timepaper.adapter.holder.EmptyHolder;
import com.github.boybeak.timepaper.adapter.holder.PhotoHolder;
import com.github.boybeak.timepaper.model.Photo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by gaoyunfei on 2017/9/4.
 */

public abstract class PaperFragment extends Fragment implements Callback<List<Photo>>,
        SwipeRefreshLayout.OnRefreshListener, DelegateParser<Photo>, OnViewEventListener<Photo, PhotoHolder> {

    private static final String TAG = PaperFragment.class.getSimpleName();

    private SwipeRefreshLayout mSrl;
    private RecyclerView mRv;

    private SuperAdapter<EmptyDelegate, FooterDelegate> mAdapter;

    private boolean isLoading = false;
    private OnScrollBottomListener mBottomListener = new OnScrollBottomListener() {
        @Override
        public void onScrollBottom(RecyclerView recyclerView, int newState) {
            if (!isLoading) {
                PaperFragment.this.onLoadData();
            }
        }
    };

    private OnViewEventListener<String, EmptyHolder> mEmptyListener
            = new OnViewEventListener<String, EmptyHolder>() {
        @Override
        public void onViewEvent(int eventCode, View view, String s, Bundle bundle,
                                EmptyHolder viewHolder, int position, DelegateAdapter adapter) {
            if (!mSrl.isRefreshing()) {
                mSrl.post(new Runnable() {
                    @Override
                    public void run() {
                        mSrl.setRefreshing(true);
                    }
                });
                onRefresh();
            }
        }

    };

    public CharSequence getTitle (Context context) {
        return this.getClass().getSimpleName();
    }

    public void scrollToTop () {
        if (mAdapter.getItemCount() > 0) {
            mRv.scrollToPosition(0);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_paper, null, true);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mSrl = view.findViewById(R.id.paper_srl);
        mSrl.setOnRefreshListener(this);

        mRv = view.findViewById(R.id.paper_rv);
        mRv.addOnScrollListener(mBottomListener);
        mAdapter = new SuperAdapter<EmptyDelegate, FooterDelegate>(getContext());
        mAdapter.setEmptyItem(new EmptyDelegate("", mEmptyListener));
        mAdapter.setTailItem(new FooterDelegate(""));
        mRv.setAdapter(mAdapter);

        Log.v(TAG, "onViewCreated " + savedInstanceState);

        if (savedInstanceState == null) {
            onLoadData();
        } else {
            ArrayList<Photo> photos = savedInstanceState.getParcelableArrayList("photos");
            int selection = savedInstanceState.getInt("selection");
            mAdapter.addAll(photos, this).autoNotify();
            mRv.scrollToPosition(selection);
        }
    }

    @Override
    public LayoutImpl parse(DelegateAdapter adapter, Photo data) {
        PhotoDelegate delegate = new PhotoDelegate(data);
        delegate.setOnViewEventListener(this);
        return delegate;
    }

    @Override
    public void onViewEvent(int eventCode, View view, Photo t, Bundle bundle, PhotoHolder viewHolder,
                            int position, DelegateAdapter adapter) {
        Intent it = new Intent(getContext(), PhotoActivity.class);
        ActivityOptionsCompat optionsCompat =
                ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(),
                        view, getString(R.string.translation_name_photo));
        it.putExtra("photo", t);
        startActivity(it, optionsCompat.toBundle());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mRv.removeOnScrollListener(mBottomListener);
    }

    @Override
    public void onResponse(@NonNull Call<List<Photo>> call, @NonNull Response<List<Photo>> response) {
        isLoading = false;
        if (mSrl.isRefreshing()) {
            mSrl.setRefreshing(false);
        }
        if (response.isSuccessful()) {
            List<Photo> photos = response.body();
            if (photos == null || photos.isEmpty()) {
                mAdapter.getTailItem().notifyEmptyState();
            } else {
                mAdapter.getTailItem().notifySuccessState();
                onResponse(photos, mAdapter);
            }
        } else {
            if (mAdapter.onlyContainsEmptyItem()) {
                try {
                    mAdapter.getEmptyItem().setSource(response.errorBody().string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mAdapter.getEmptyItem().notifyFailedState();
                mAdapter.notifyEmpty();
            }
            if (mAdapter.containsTailItem()) {
                mAdapter.getTailItem().notifyFailedState();
            }
        }
        mAdapter.notifyTail();
    }

    public void onResponse (List<Photo> photos, SuperAdapter<EmptyDelegate, FooterDelegate> adapter) {
        mAdapter.addAll(photos, this).autoNotify();
    }

    @Override
    public void onFailure(@NonNull Call<List<Photo>> call, @NonNull Throwable t) {
        isLoading = false;
        if (mSrl.isRefreshing()) {
            mSrl.setRefreshing(false);
        }
        if (mAdapter.onlyContainsEmptyItem()) {
            mAdapter.getEmptyItem().setSource(t.getMessage());
            mAdapter.getEmptyItem().notifyFailedState();
            mAdapter.notifyEmpty();
        }
        if (mAdapter.containsTailItem()) {
            mAdapter.getTailItem().notifyFailedState();
        }
        onFailure(call, t, mAdapter);
    }

    public abstract void onFailure (@NonNull Call<List<Photo>> call, @NonNull Throwable t,
                                    SuperAdapter<EmptyDelegate, FooterDelegate> adapter);

    public void onLoadData() {
        isLoading = true;
        if (mAdapter.onlyContainsEmptyItem()) {
            mAdapter.getEmptyItem().notifyLoadingState();
            mAdapter.notifyEmpty();
        } else if (mAdapter.containsTailItem()) {
            mAdapter.getTailItem().notifyLoadingState();
            mAdapter.notifyTail();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        Log.v(TAG, "setUserVisibleHint " + getClass().getSimpleName() + " " + isVisibleToUser);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        List<Photo> photos = mAdapter.selector(PhotoDelegate.class).extractAll(
                Path.with(PhotoDelegate.class, Photo.class).methodWith("getSource")
        );
        if (photos != null && !photos.isEmpty()) {
            ArrayList<Photo> photoArrayList = new ArrayList<>();
            photoArrayList.addAll(photos);
            int selection = ((LinearLayoutManager)mRv.getLayoutManager()).findFirstVisibleItemPosition();
            outState.putParcelableArrayList("photos", photoArrayList);
            outState.putInt("selection", selection);
        }
        super.onSaveInstanceState(outState);
        Log.v(TAG, "onSaveInstanceState ");
    }

    @Override
    public void onRefresh() {
        onLoadData();
    }
}
