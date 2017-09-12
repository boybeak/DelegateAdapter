package com.github.boybeak.timepaper;

import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.util.Log;
import android.view.View;

/**
 * Created by gaoyunfei on 2017/9/9.
 */

public class ProfileBehavior extends AppBarLayout.Behavior {

    private static final String TAG = ProfileBehavior.class.getSimpleName();

    @Override
    public boolean onNestedFling(CoordinatorLayout coordinatorLayout, AppBarLayout child, View target, float velocityX, float velocityY, boolean consumed) {
        Log.v(TAG, "onNestedFling");
        return super.onNestedFling(coordinatorLayout, child, target, velocityX, velocityY, consumed);
    }
}
