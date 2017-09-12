package com.nulldreams.delegateadapter.adapter.delegate;

import com.github.boybeak.adapter.AbsDelegate;
import com.github.boybeak.adapter.annotation.AnnotationDelegate;
import com.github.boybeak.adapter.annotation.DelegateInfo;
import com.nulldreams.delegateadapter.R;
import com.nulldreams.delegateadapter.User.User;
import com.nulldreams.delegateadapter.adapter.holder.UserHolder;

/**
 * Created by gaoyunfei on 2017/9/13.
 */
@DelegateInfo(layoutID = R.layout.layout_user, holderClass = UserHolder.class)
public class UserDelegate extends AnnotationDelegate<User, UserHolder> {

    public UserDelegate(User t) {
        super(t);
    }
}
