package com.nulldreams.delegateadapter.adapter;

import com.github.boybeak.adapter.annotation.AnnotationDelegate;
import com.github.boybeak.adapter.annotation.DelegateInfo;
import com.github.boybeak.adapter.annotation.OnClick;
import com.nulldreams.delegateadapter.R;
import com.nulldreams.delegateadapter.model.User;
import com.nulldreams.delegateadapter.adapter.event.UserClickListener;

/**
 * Created by gaoyunfei on 2016/12/18.
 */
@DelegateInfo(layoutID = R.layout.layout_user, holderClass = UserHolder.class)
public class UserDelegate extends AnnotationDelegate<User> {
    @OnClick
    public Class<UserClickListener> onClick = UserClickListener.class;
    public UserDelegate(User user) {
        super(user);
    }
}
