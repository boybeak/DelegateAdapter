package com.github.boybeak.timepaper.adapter.delegate;

import com.github.boybeak.adapter.OnViewEventListener;
import com.github.boybeak.adapter.annotation.AnnotationDelegate;
import com.github.boybeak.adapter.annotation.DelegateInfo;
import com.github.boybeak.timepaper.R;
import com.github.boybeak.timepaper.adapter.holder.UserRowHolder;
import com.github.boybeak.timepaper.model.User;

/**
 * Created by gaoyunfei on 2017/9/6.
 */
@DelegateInfo(layoutID = R.layout.layout_user_row, holderClass = UserRowHolder.class)
public class UserRowDelegate extends AnnotationDelegate<User, UserRowHolder> {

    public static final int PROFILE_CLICK = 1;

    public UserRowDelegate(User user, OnViewEventListener<User, UserRowHolder> onViewEventListener) {
        super(user, onViewEventListener);
    }
}
