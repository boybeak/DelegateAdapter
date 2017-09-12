package com.nulldreams.delegateadapter.adapter.holder;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;

import com.bumptech.glide.Glide;
import com.github.boybeak.adapter.AbsViewHolder;
import com.github.boybeak.adapter.DelegateAdapter;
import com.nulldreams.delegateadapter.R;
import com.nulldreams.delegateadapter.User.User;
import com.nulldreams.delegateadapter.adapter.delegate.UserDelegate;

/**
 * Created by gaoyunfei on 2017/9/13.
 */

public class UserHolder extends AbsViewHolder<UserDelegate> {

    private AppCompatImageView profileIv;
    private AppCompatTextView nameTv, bioTv;

    public UserHolder(View itemView) {
        super(itemView);

        profileIv = findViewById(R.id.profile);
        nameTv = findViewById(R.id.name);
        bioTv = findViewById(R.id.bio);
    }

    @Override
    public void onBindView(Context context, UserDelegate t, int position, DelegateAdapter adapter) {
        User user = t.getSource();
        Glide.with(context).load(user.profile).into(profileIv);
        nameTv.setText(user.name);
        bioTv.setText(user.bio);
    }
}
