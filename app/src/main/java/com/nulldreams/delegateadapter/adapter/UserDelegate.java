package com.nulldreams.delegateadapter.adapter;

import com.nulldreams.adapter.annotation.AnnotationDelegate;
import com.nulldreams.adapter.annotation.DelegateInfo;
import com.nulldreams.delegateadapter.R;
import com.nulldreams.delegateadapter.User;

/**
 * Created by gaoyunfei on 2016/12/18.
 */
@DelegateInfo(layoutID = R.layout.layout_user, holderClass = UserHolder.class)
public class UserDelegate extends AnnotationDelegate<User> {
    public UserDelegate(User user) {
        super(user);
    }
}
