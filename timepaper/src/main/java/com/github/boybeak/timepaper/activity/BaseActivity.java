package com.github.boybeak.timepaper.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.boybeak.adapter.AbsDelegate;
import com.github.boybeak.adapter.DelegateAdapter;
import com.github.boybeak.adapter.OnViewEventListener;
import com.github.boybeak.adapter.extension.SuperAdapter;
import com.github.boybeak.timepaper.R;
import com.github.boybeak.timepaper.adapter.delegate.FooterDelegate;
import com.github.boybeak.timepaper.adapter.delegate.LogDelegate;
import com.github.boybeak.timepaper.adapter.holder.LogHolder;

/**
 * Created by gaoyunfei on 2017/9/4.
 */

public class BaseActivity extends AppCompatActivity {

    private View mWaitingView;
    private RecyclerView mWaitingRv;
    private SuperAdapter<FooterDelegate, FooterDelegate> mLogAdapter;

    private OnViewEventListener<String, LogHolder> mLogListener =
            new OnViewEventListener<String, LogHolder>() {
                @Override
                public void onViewEvent(int eventCode, View view, String s, Bundle bundle, LogHolder viewHolder, int position, DelegateAdapter adapter) {
                    hideWaitingMask();
                }

    };

    @Override
    public void onBackPressed() {
        if (isWaiting()) {
            hideWaitingMask();
            return;
        }
        super.onBackPressed();
    }

    public void addLog (String logMsg) {
        if (mLogAdapter != null) {
            mLogAdapter.add(new LogDelegate(logMsg, mLogListener)).autoNotify();
        }
    }

    private boolean isWaiting () {
        return mWaitingView != null;
    }

    public void showWaitingMask () {

        if (mWaitingView != null) {
            return;
        }

        ViewGroup contentView = (ViewGroup)findViewById(android.R.id.content);

        mWaitingView = LayoutInflater.from(this).inflate(R.layout.layout_waiting_mask, null, false);
        contentView.addView(mWaitingView);

        mWaitingRv = mWaitingView.findViewById(R.id.waiting_mask);

        mLogAdapter = new SuperAdapter<FooterDelegate, FooterDelegate>(this);
        FooterDelegate mFooterDelegate = new FooterDelegate("");
        mLogAdapter.setTailItem(mFooterDelegate);
        mWaitingRv.setAdapter(mLogAdapter);
    }

    public void hideWaitingMask () {
        if (mWaitingView != null) {
            ViewGroup contentView = (ViewGroup)findViewById(android.R.id.content);
            contentView.removeView(mWaitingView);
            mLogAdapter.clear();
            mLogAdapter = null;
            mWaitingView = null;
            mWaitingRv = null;
        }
    }
}
